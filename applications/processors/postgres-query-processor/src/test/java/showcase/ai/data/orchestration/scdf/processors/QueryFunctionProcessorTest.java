package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.LinkedCaseInsensitiveMap;
import showcase.ai.data.orchestration.scdf.properties.QueryProperties;

import javax.sql.DataSource;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@SpringJUnitConfig
@SpringRabbitTest
class QueryFunctionProcessorTest {

    @Autowired
    private QueryFunctionProcessor subject;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


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

    @Test
    void validJson() {


        var sql = """
                select :firstName as firstName,
                       :lastName as lastName
                """;
        var payload = """
                {
                    "firstName": "John",
                    "lastName": "Smith"
                }
                """;
        var queryProperties = new QueryProperties(sql);

        subject = new QueryFunctionProcessor(namedParameterJdbcTemplate,objectMapper,queryProperties);

        var actual = subject.apply(payload);

        assertDoesNotThrow( () -> objectMapper.readTree(actual));

    }

    @Test
    void mapToJson() throws JsonProcessingException {

        var feedback = """
                "Paris is the capital and most populous city of France, with an estimated population of 2,175,601 residents as of 2018, in an area of more than 105 square kilometres (41 square miles). The City of Paris is the centre and seat of government of the region and province of Île-de-France, or Paris Region, which has an estimated population of 12,174,880, or about 18 percent of the population of France as of 2017., summary="The City of Paris is the centre and seat of government of the region and province of le-de-France, or Paris Region, which has an estimated population of 12,174,880, or about 18 percent of the population of France as of 2017."
                """;

        var map= new LinkedCaseInsensitiveMap<String>();


        map.put("id", "F001");
        map.put("email", "jmatthews@email");
        map.put("feedback", feedback);

        var actual = objectMapper.writeValueAsString(map);

        System.out.printf(actual);

    }
}