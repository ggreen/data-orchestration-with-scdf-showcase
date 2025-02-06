package showcase.ai.data.orchestration.scdf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.ai.data.orchestration.scdf.properties.SqlConsumerProperties;

@Configuration
@EnableConfigurationProperties(SqlConsumerProperties.class)
public class AppConfig {

}
