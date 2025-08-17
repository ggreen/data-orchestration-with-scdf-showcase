package showcase.ai.data.orchestration.postgres.embedding.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;
import showcase.ai.data.orchestration.postgres.embedding.properties.EmbeddingSimilarityProperties;

import java.util.List;
import java.util.function.Function;

import static java.util.List.of;

/**
 *
 * Save payload as embedding
 * @author Gregory Green
 */
@Component
public class EmbeddingSimilarityFunction implements Function<String,List<Document> > {
    private final VectorStore vectorStore;
    private final EmbeddingSimilarityProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmbeddingSimilarityFunction(VectorStore vectorStore, EmbeddingSimilarityProperties properties) {
        this.vectorStore = vectorStore;
        this.properties = properties;
    }

    @Override
    public List<Document> apply(String payload) {
        vectorStore.add(of(Document
                .builder().text(payload).build()));


        return vectorStore.similaritySearch(SearchRequest.builder().query(payload)
                .topK(properties.getTopK())
                        .similarityThreshold(properties.getSimilarityThreshold())
                .filterExpression(
                        new FilterExpressionBuilder()
                                .ne("id",getId(payload)).build())
                .build());

    }

    @SneakyThrows
    String getId(String payload) {
        var jsonNode = objectMapper.readTree(payload);

        var idNode = jsonNode.findValue("id");

        return idNode != null? idNode.asText() : null;

    }
}
