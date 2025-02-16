package showcase.ai.data.orchestration.scdf.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "sql.consumer")
public class SqlConsumerProperties {

    /**
     * Sql to execute. Note the Json payload is passed as SQL parameters based on field names.
     */
    private String sql;
}
