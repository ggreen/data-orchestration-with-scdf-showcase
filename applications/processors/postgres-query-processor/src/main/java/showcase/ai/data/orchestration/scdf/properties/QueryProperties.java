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
@ConfigurationProperties(prefix = "query.processor")
public class QueryProperties {

    /**
     * Query Sql statement ex: select "1" as id
     */
    private String sql;
}
