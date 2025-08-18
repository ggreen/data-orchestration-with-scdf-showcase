package spring.gemfire.batch.account.batch;

import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.creational.generator.FullNameCreator;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.util.Text;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import spring.gemfire.showcase.account.domain.account.Customer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

import static java.lang.String.valueOf;

@Configuration
@Slf4j
public class LoadDataConfig {

    @Value("${db.schema}")
    private String schemaName;

    private String insertSql  = """
                INSERT INTO ${schemaName}.account (
                    id, 
                    "name",
                    "first_nm",
                    "last_nm",
                    "email",
                    "phone"
                ) 
                VALUES(?,?,?,?,?,?)
                """;

    private String deleteSql  = """
                truncate ${schemaName}.account
                """;

    @Value("${account.data.count:100}")
    private int accountCount;

    @Value("${account.data.batch.size:10}")
    private int batchSize;

    @Value("${account.data.prefix:LLC}")
    private String accountNamePrefix;

    @ConditionalOnProperty( name= "batch.load.accounts", havingValue = "true")
    @Order(9)
    @Bean
    CommandLineRunner loadData(DataSource dataSource)
    {
        var map = Map.of("schemaName",schemaName);
        insertSql = Text.format(insertSql,map);
        deleteSql  = Text.format(deleteSql,map);

        var fullNameCreator = new FullNameCreator();

        JavaBeanGeneratorCreator<Customer> customerCreator = JavaBeanGeneratorCreator.of(Customer.class);

        return args -> {
            log.info("Inserting accounts {}",insertSql);

            try (var conn = dataSource.getConnection();
                 var statement = conn.prepareStatement(insertSql); var deleteStatement = conn.createStatement()) {

                log.info("Delete account data SQL: {}",deleteSql);
                deleteStatement.execute(deleteSql);

                for (int i = 0; i < accountCount; i++) {
                    Customer customer = customerCreator.create();
                    statement.setString(1, valueOf(i));
                    statement.setString(2, accountNamePrefix +" "+i+" "+fullNameCreator.create());
                    statement.setString(3,customer.getFirstName());
                    statement.setString(4,customer.getLastName());
                    statement.setString(5,customer.getContact().getEmail());
                    statement.setString(6,customer.getContact().getPhone());

                    statement.addBatch();

                    if((i+1) % batchSize == 0)
                    {
                        statement.executeBatch();
                        log.info("executed batch size: {}",i);
                    }

                }

                statement.executeBatch();

            } catch (SQLException e) {
                log.warn("Cannot records",e);
            }
        };
    }
}
