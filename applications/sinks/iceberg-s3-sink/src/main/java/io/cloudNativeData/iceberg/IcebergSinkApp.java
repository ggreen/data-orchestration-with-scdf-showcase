package io.cloudNativeData.iceberg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IcebergSinkApp {
    public static void main(String[] args) {
        SpringApplication.run(IcebergSinkApp.class, args);
    }
}
