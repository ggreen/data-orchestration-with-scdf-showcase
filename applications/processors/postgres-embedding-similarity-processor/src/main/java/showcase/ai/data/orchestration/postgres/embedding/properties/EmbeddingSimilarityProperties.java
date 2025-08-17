package showcase.ai.data.orchestration.postgres.embedding.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "embedding.similarity.processor")
public class EmbeddingSimilarityProperties {

    /**
     * top K most similar vectors to a query vector in a vector database
     */
    private int topK;

    /**
     * The lower bound of the similarity score.
     */
    private double similarityThreshold;
}
