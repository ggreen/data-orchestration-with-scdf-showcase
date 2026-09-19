package io.cloudNativeData.iceberg.functions;

import io.cloudNativeData.iceberg.repository.IcebergTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class IceBergConsumer implements Consumer<Map<String, Object>> {

    private final IcebergTemplate icebergTemplate;

    @Override
    public void accept(Map<String, Object> stringObjectMap) {
        icebergTemplate.writeRecord(stringObjectMap);
    }
}
