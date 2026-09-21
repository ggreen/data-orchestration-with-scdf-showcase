package io.cloudNativeData.iceberg.controller;

import io.cloudNativeData.iceberg.service.IceBergService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("iceberg")
@RequiredArgsConstructor
@Slf4j
public class IceBergController {

    private final IceBergService service;

    @PostMapping
    public void writeRecord(Map<String, Object> record) {

        log.info("iceberg write record: {}", record);
        service.save(record);
    }


    @GetMapping
    public Iterable<Map<String, Object>> findAll() {
        return service.findAll();
    }
}
