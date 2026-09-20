package io.cloudNativeData.iceberg.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "iceberg.schemas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchemaProperties {

    private NamespaceProperties namespace;
    private String tableName;
    private List<FieldProperties> fields;
}
