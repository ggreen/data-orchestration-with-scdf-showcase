package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@SpringJUnitConfig
@SpringRabbitTest
class QueryProcessorFunctionTest {

    @Autowired
    private QueryProcessorFunction subject;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() {
        System.setProperty(
                "query.sql",
                "select :firstName");
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void accept() throws JsonProcessingException {
        var expected  = """
                {"HELLO":"world"}
                """;

        var payload = """
                { "email" : "${email}" ,  "firstName" : "${firstName}" }
                """;

        var actual = subject.apply(payload);

        assertThat(actual).isNotNull();
        assertThat(actual.trim()).isEqualTo(expected.trim());

    }
}