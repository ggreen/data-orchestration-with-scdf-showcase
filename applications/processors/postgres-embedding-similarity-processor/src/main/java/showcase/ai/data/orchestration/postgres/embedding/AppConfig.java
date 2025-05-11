package showcase.ai.data.orchestration.postgres.embedding;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.ai.data.orchestration.postgres.embedding.properties.EmbeddingSimilarityProperties;

@Configuration
@EnableConfigurationProperties(EmbeddingSimilarityProperties.class)
public class AppConfig {

}
