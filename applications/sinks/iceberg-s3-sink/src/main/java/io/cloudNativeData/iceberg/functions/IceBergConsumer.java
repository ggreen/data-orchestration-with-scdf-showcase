package io.cloudNativeData.iceberg.functions;

import io.cloudNativeData.iceberg.service.IceBergService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class IceBergConsumer implements Consumer<Map<String, Object>> {

    private final IceBergService service;

    @Override
    public void accept(Map<String, Object> map) {
        service.save(map);
    }
}
