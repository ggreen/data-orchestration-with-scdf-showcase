package io.cloudNativeData.iceberg.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldProperties {
    private int id;
    private String name;
    private String type; // e.g., "long", "string"
    private boolean required;
}

