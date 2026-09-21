package io.cloudNativeData.iceberg.controller;

import io.cloudNativeData.iceberg.service.IceBergService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IceBergControllerTest {

    private final Map<String, Object> record = Map.of("id","1");

    @Mock
    private IceBergService service;
    private IceBergController subject;

    @BeforeEach
    void setUp() {
        subject = new IceBergController(service);
    }

    @Test
    void saveData() {
        subject.writeRecord(record);

        verify(service).save(any());
    }

    @Test
    void findAll() {
        Iterable<Map<String, Object>> expected = List.of(record);
        when(service.findAll()).thenReturn(expected);
        var actual = subject.findAll();

        assertThat(actual).isEqualTo(expected);
    }
}