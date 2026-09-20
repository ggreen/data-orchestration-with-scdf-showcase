package io.cloudNativeData.iceberg;

import io.cloudNativeData.iceberg.properties.FieldProperties;
import io.cloudNativeData.iceberg.properties.SchemaProperties;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.creational.Creator;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.data.parquet.GenericParquetWriter;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.io.OutputFileFactory;
import org.apache.iceberg.parquet.Parquet;
import org.apache.iceberg.types.Type;
import org.apache.iceberg.types.Types;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class IcebergConfig {

    @Value("${iceberg.s3.warehouse.path:s3a://iceberg-bucket/warehouse}")
    private String s3WarehousePath;

    @Value("${iceberg.s3.connection.url:http://localhost:9000}")
    private String connectionUrl;

    @Value("${iceberg.s3.connection.username:admin}")
    private String username;

    @Value("${iceberg.s3.connection.password:password123}")
    private String password;

    @Value("${iceberg.s3.connection.sslEnabled:false}")
    private String sslEnabled;

    @Bean
    HadoopCatalog  hadoopCatalog()
    {
        var conf = new org.apache.hadoop.conf.Configuration();

        // Target MinIO / Local S3 endpoint

        conf.set("fs.s3a.endpoint", connectionUrl);
        conf.set("fs.s3a.access.key", username);
        conf.set("fs.s3a.secret.key", password);
        conf.set("fs.s3a.threads.keepalivetime","60");

        // Essential S3A settings for MinIO / Local S3
        conf.set("fs.s3a.path.style.access", "true"); // Required for MinIO
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.connection.ssl.enabled", sslEnabled); // Disable SSL for local HTTP
        conf.set("fs.s3a.connection.timeout", "30000");         // 30,000 ms instead of "30s"
        conf.set("fs.s3a.connection.establish.timeout", "30000"); // 30,000 ms instead of "30s"

        // FIX: Force Hadoop S3A to use AWS SDK v1 simple credentials provider
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        // Initialize Hadoop Catalog pointing to S3
        return new HadoopCatalog(conf, s3WarehousePath);
    }

    @Bean
    Schema schema(SchemaProperties schemaProperties)
    {
        List<Types.NestedField> nestedFields = new ArrayList<>();
        for (FieldProperties field : schemaProperties.getFields()) {

            Type iceBergType = switch (field.getType().toLowerCase()) {
                case "long" -> Types.LongType.get();
                case "string" -> Types.StringType.get();
                case "int", "integer" -> Types.IntegerType.get();
                case "boolean" -> Types.BooleanType.get();
                case "double" -> Types.DoubleType.get();
                default -> throw new IllegalArgumentException("Unsupported Iceberg type: " + field.getType());
            };

            Types.NestedField nestedField;
            if (field.isRequired()) {
                nestedField = Types.NestedField
                        .required(field.getId(), field.getName(), iceBergType);
            } else {
                nestedField = Types.NestedField.optional(field.getId(), field.getName(), iceBergType);
            }
            nestedFields.add(nestedField);
        }
        return new Schema(nestedFields);
    }

    @Bean
    Table table(HadoopCatalog catalog, Schema schema, SchemaProperties schemaProperties){
        var name = TableIdentifier
                .of(schemaProperties.getNamespace().getName(),
                schemaProperties.getTableName());

        // 4. Create the table (Unpartitioned)
        Table table;
        if (catalog.tableExists(name)) {
            table = catalog.loadTable(name);
            log.info("Table already exists. Loaded existing table.");
        } else {
            table = catalog.createTable(name, schema, PartitionSpec.unpartitioned());
            log.info("Created new Iceberg table at: {}", table.location());
        }
        return table;
    }

    @Bean
    Creator<Record> recordCreator(Schema schema) {
        return () -> GenericRecord.create(schema);
    }

    @Bean
    Creator<DataWriter<Record>> dataWriterProvider(Table table, Schema schema)
    {

        return () -> {
            var fileFactory = OutputFileFactory.builderFor(table, 1, 1).build();

            try {
                return Parquet.writeData(fileFactory.newOutputFile())
                        .schema(schema)
                        .createWriterFunc(GenericParquetWriter::buildWriter)
                        .forTable(table)
                        .withSpec(PartitionSpec.unpartitioned())
                        .build();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}
