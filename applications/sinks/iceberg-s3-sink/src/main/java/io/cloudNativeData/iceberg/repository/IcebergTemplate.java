package io.cloudNativeData.iceberg.repository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nyla.solutions.core.patterns.creational.Creator;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.CloseableIterable;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.types.Types;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IcebergTemplate {
    private final Creator<DataWriter<org.apache.iceberg.data.Record>> dataWriterProvider;
    private final Creator<org.apache.iceberg.data.Record> recordProvider;
    private final Schema schema;
    private final Table table;

    @SneakyThrows
    public void writeRecord(Map<String, Object> row) {

        DataWriter<org.apache.iceberg.data.Record> writer = null;

        try{
            writer = dataWriterProvider.create();
            var record = recordProvider.create();
            row.forEach(record::setField);
            writer.write(record);

        }
        finally {
            if(writer != null)
            {
                writer.close();

                var dataFile = writer.toDataFile();
                table.newAppend().appendFile(dataFile)
                        .commit();
            }

        }
    }

    @SneakyThrows
    public Iterable<Map<String, Object>> findAll() {
        List<Map<String, Object>> results = new ArrayList<>();

        // Verify table has a current snapshot before reading
        if (this.table == null || this.table.currentSnapshot() == null) {
            return results;
        }

        // Build the reader scan using the IcebergGenerics API
        try (CloseableIterable<Record> reader = IcebergGenerics.read(this.table)
                .useSnapshot(this.table.currentSnapshot().snapshotId())
                .build()) {

            for (Record record : reader) {
                Map<String, Object> row = new HashMap<>();

                // Dynamically extract values based on the schema fields
                for (Types.NestedField field : this.schema.columns()) {
                    String fieldName = field.name();
                    Object value = record.getField(fieldName);
                    row.put(fieldName, value);
                }

                results.add(row);
            }

        }

        return results;
    }
}
