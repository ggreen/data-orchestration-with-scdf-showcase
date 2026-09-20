package io.cloudNativeData.iceberg.controller;

import io.cloudNativeData.iceberg.functions.IceBergConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("iceberg")
@RequiredArgsConstructor
@Slf4j
public class IceBergController {

    private final IceBergConsumer iceBergConsumer;

    public void writeRecord(Map<String, Object> record) {

        log.info("iceberg write record: {}", record);
        iceBergConsumer.accept(record);
    }
}
