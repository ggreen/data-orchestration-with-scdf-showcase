package io.cloudNativeData.iceberg;

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
import org.apache.iceberg.types.Types;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

@Configuration
@Slf4j
public class IcebergConfig {

    @Value("${app.wareHouse.path:./runtime/iceberg_warehouse}")
    private String wareHousePath;

    @Bean
    HadoopCatalog  hadoopCatalog()
    {
        var warehousePath = new File(wareHousePath).getAbsolutePath();
        var conf = new org.apache.hadoop.conf.Configuration();

        // 2. Initialize Hadoop Catalog pointing to local filesystem
        return new HadoopCatalog(conf, warehousePath);
    }

    @Bean
    Schema schema(HadoopCatalog catalog)
    {
        var schema = new Schema(
                Types.NestedField.required(1, "id", Types.LongType.get()),
                Types.NestedField.required(2, "name", Types.StringType.get()),
                Types.NestedField.optional(3, "email", Types.StringType.get())
        );

        var name = TableIdentifier.of("default_db", "users");

        return schema;

    }

    @Bean
    Table table(HadoopCatalog catalog, Schema schema){
        var name = TableIdentifier.of("default_db", "users");

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
