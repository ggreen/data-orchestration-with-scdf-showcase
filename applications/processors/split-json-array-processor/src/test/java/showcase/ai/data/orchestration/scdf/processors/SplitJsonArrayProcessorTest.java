package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Split input JSON array to individual messages
 * @author gregory green
 */
class SplitJsonArrayProcessorTest {

    private final SplitJsonArrayProcessor subject = new SplitJsonArrayProcessor();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenJsonThenReturnMessages() throws JsonProcessingException {

        var given = """
                [{"id" : "1"},{"id" : "2"}]
                """;
        var actual = subject.apply(given);

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(2);

        for (var item : actual){
            System.out.println(item.getPayload());
            assertThat(item.getPayload()).isNotEmpty();
            assertThat(item.getPayload()).isInstanceOf(String.class);
            objectMapper.readTree(item.getPayload().toString());
        }

    }

    @Test
    void givenNullOrEmptyThenReturnNothing() {
        assertThat(subject.apply(null)).isNullOrEmpty();
        assertThat(subject.apply("")).isNullOrEmpty();
        assertThat(subject.apply(" ")).isNullOrEmpty();
    }

    @Test
    @EnabledIfSystemProperty(named = "runSplitIntTest", matches = "true")
    void integrationTest() throws IOException {

        var json = IO.readFile(new File("src/test/resources/secret/boxscore.json"));

        var actual = subject.apply(json);

        assertThat(actual).isNotEmpty();

        for (var item : actual){
            System.out.println(item.getPayload());
            assertThat(item.getPayload()).isNotEmpty();
            assertThat(item.getPayload()).isInstanceOf(String.class);
        }

    }
}