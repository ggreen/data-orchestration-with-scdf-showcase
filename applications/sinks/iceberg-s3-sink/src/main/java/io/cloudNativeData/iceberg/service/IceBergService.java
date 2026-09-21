package io.cloudNativeData.iceberg.service;

import io.cloudNativeData.iceberg.repository.IcebergTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IceBergService {
    private final IcebergTemplate template;

    public void save(Map<String, Object> record) {
        log.info("Iceberg format received data: {}", record);
        template.writeRecord(record);
    }

    public Iterable<Map<String, Object>> findAll() {
        log.info("Iceberg readind all data");
        return template.findAll();
    }
}
