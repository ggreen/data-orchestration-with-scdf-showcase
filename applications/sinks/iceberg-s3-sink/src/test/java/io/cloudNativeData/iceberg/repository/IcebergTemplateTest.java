package io.cloudNativeData.iceberg.repository;

import nyla.solutions.core.patterns.creational.Creator;
import org.apache.iceberg.AppendFiles;
import org.apache.iceberg.DataFile;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.DataWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    @Mock
    private Table table;
    @Mock
    private DataFile dataFile;
    @Mock
    private AppendFiles appendFile;


    @BeforeEach
    void setUp() {
        subject = new IcebergTemplate(objectProvider,recordProvider, schema,table);
    }

    @Test
    void save() {
        when(objectProvider.create()).thenReturn(dataWriter);
        when(recordProvider.create()).thenReturn(record);
        when(dataWriter.toDataFile()).thenReturn(dataFile);
        when(table.newAppend()).thenReturn(appendFile);
        when(appendFile.appendFile(any(DataFile.class))).thenReturn(appendFile);

        subject.writeRecord(map);

    }

    @Test
    void findAll() {
        Iterable<Map<String,Object>> actual = subject.findAll();
        assertThat(actual).isEmpty();
    }
}