package io.cloudNativeData.iceberg.functions;

import io.cloudNativeData.iceberg.service.IceBergService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IceBergConsumerTest {

    private IceBergConsumer subject;
    @Mock
    private Map<String, Object> map;

    @Mock
    private IceBergService service;

    @BeforeEach
    void setUp() {
        subject = new IceBergConsumer(service);
    }

    @Test
    void given_record_when_accept_then_write_to_iceberg() {

        subject.accept(map);

        verify(service).save(ArgumentMatchers.<Map<String,Object>>any());

    }
}