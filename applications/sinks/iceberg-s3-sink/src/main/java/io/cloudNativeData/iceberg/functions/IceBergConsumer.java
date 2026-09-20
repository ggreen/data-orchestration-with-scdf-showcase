package io.cloudNativeData.iceberg.functions;

import io.cloudNativeData.iceberg.repository.IcebergTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class IceBergConsumer implements Consumer<Map<String, Object>> {

    private final IcebergTemplate icebergTemplate;

    @Override
    public void accept(Map<String, Object> map) {

        log.info("Iceberg consumer received data: {}", map);
        icebergTemplate.writeRecord(map);
    }
}
