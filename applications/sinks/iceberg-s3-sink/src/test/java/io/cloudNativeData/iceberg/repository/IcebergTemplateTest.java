package io.cloudNativeData.iceberg.repository;

import nyla.solutions.core.patterns.creational.Creator;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.io.OutputFileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.de.siegmar.fastcsv.writer.CsvWriter;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IcebergTemplateTest {

    @Mock
    private DataWriter<org.apache.iceberg.data.Record> dataWriter;
    @Mock
    private Schema schema;
    private final Map<String,Object> map = Map.of("test","1");

    private IcebergTemplate subject;
    @Mock
    private Creator<DataWriter<org.apache.iceberg.data.Record>> objectProvider;
    @Mock
    private Creator<org.apache.iceberg.data.Record> recordProvider;
    @Mock
    private Record record;


    @BeforeEach
    void setUp() {
        subject = new IcebergTemplate(objectProvider,recordProvider, schema);
    }

    @Test
    void save() {
        when(objectProvider.create()).thenReturn(dataWriter);
        when(recordProvider.create()).thenReturn(record);

        subject.writeRecord(map);

    }
}