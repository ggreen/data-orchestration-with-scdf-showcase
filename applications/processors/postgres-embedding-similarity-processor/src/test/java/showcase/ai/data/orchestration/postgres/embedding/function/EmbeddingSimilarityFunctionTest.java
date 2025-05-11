package showcase.ai.data.orchestration.postgres.embedding.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import showcase.ai.data.orchestration.postgres.embedding.properties.EmbeddingSimilarityProperties;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmbeddingSimilarityFunctionTest {

    private static final String expectedId = "001";
    private static final String payload = """
            { "id" : "001"}
            """;

    @Mock
    private VectorStore vectorStore;
    private EmbeddingSimilarityFunction subject;
    private static final EmbeddingSimilarityProperties properties = new EmbeddingSimilarityProperties(4);

    @BeforeEach
    void setUp() {
        subject = new EmbeddingSimilarityFunction(vectorStore,properties);
    }

    @Test
    void accept() {
        var actual = subject.apply(payload);

        verify(vectorStore).add(ArgumentMatchers.<List<Document>>any());

        verify(vectorStore).similaritySearch(any(SearchRequest.class));
    }

    @Test
    void getId() {

        var  actual = subject.getId(payload);

        assertThat(actual).isEqualTo(expectedId);

    }
}