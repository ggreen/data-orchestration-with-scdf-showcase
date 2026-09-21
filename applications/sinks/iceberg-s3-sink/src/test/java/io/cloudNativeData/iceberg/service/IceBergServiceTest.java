package io.cloudNativeData.iceberg.service;

import io.cloudNativeData.iceberg.repository.IcebergTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IceBergServiceTest {

    private IceBergService subject;

    @Mock
    private Map<String, Object> record;
    @Mock
    private IcebergTemplate template;


    @BeforeEach
    void setUp() {
        subject = new IceBergService(template);
    }

    @Test
    void read(){
        Iterable<Map<String,Object>> expected = List.of(record);
        when(template.findAll()).thenReturn(expected);

        var actual = subject.findAll();

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void icebergWriteRecord() {
        subject.save(record);

        verify(template).writeRecord(any());
    }

}