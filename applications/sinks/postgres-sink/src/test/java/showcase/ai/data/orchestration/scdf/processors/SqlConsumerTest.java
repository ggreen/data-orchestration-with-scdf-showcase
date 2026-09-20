package showcase.ai.data.orchestration.scdf.processors;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import showcase.ai.data.orchestration.scdf.data.Customer;
import tools.jackson.databind.json.JsonMapper;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@SpringJUnitConfig
@SpringRabbitTest
class SqlConsumerTest {

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public JsonMapper jsonMapper() {
            return new JsonMapper();
        }

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .setName("testdb") // Optional: names the database
                    .build();
        }


        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource)
        {
            return new JdbcTemplate(dataSource);
        }

     }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlConsumer subject;

//    @Autowired
//    private JsonMapper objectMapper;

    private final Customer customer = JavaBeanGeneratorCreator.of(Customer.class).create();


    @BeforeAll
    static void beforeAll() {
        System.setProperty(
                "sql.consumer.sql",
                "insert into customers(email,first_name) values(:email,:firstName)");
    }

    @BeforeEach
    void setUp() {
        final String createSql = """
                CREATE TABLE IF NOT EXISTS customers(email VARCHAR(255) PRIMARY KEY, first_name VARCHAR(255));
                """;
        jdbcTemplate.execute(createSql);
    }

    @Test
    void accept() {
        String payload = """
                { "email" : "${email}" ,  "firstName" : "${firstName}" }
                """;

        payload = Text.format().formatMap(payload, JavaBean.toMap(customer));
        subject.accept(payload);

        String query = """
                select first_name from customers where email = ?
                """;

        var name = jdbcTemplate.queryForObject(query, String.class, customer.getEmail());

        assertThat(name).isEqualTo(customer.getFirstName());

    }
}