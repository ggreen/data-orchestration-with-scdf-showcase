package io.cloudNativeData.iceberg.repository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nyla.solutions.core.patterns.creational.Creator;
import org.apache.iceberg.Schema;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.io.DataWriter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class IcebergTemplate {
    private final Creator<DataWriter<org.apache.iceberg.data.Record>> dataWriterProvider;
    private final Creator<org.apache.iceberg.data.Record> recordProvider;
    private final Schema schema;

    @SneakyThrows
    public void writeRecord(Map<String, Object> row) {
       try(DataWriter<org.apache.iceberg.data.Record> writer = dataWriterProvider.create())
       {
           var record = recordProvider.create();
           row.forEach(record::setField);
           writer.write(record);
       }

    }
}
