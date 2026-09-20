package io.cloudNativeData.iceberg.controller;

import io.cloudNativeData.iceberg.functions.IceBergConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IceBergControllerTest {

    private final Map<String, Object> record = Map.of("id","1");

    @Mock
    private IceBergConsumer icebergConsumer;
    private IceBergController subject;

    @BeforeEach
    void setUp() {
        subject = new IceBergController(icebergConsumer);
    }

    @Test
    void saveData() {
        subject.writeRecord(record);

        verify(icebergConsumer).accept(any());
    }
}