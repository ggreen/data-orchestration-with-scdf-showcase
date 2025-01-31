package showcase.ai.data.orchestration.scdf.sink;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import showcase.ai.data.orchestration.scdf.properties.SqlConsumerProperties;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class SqlConsumer implements Consumer<String> {

    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
    private final String sql;

    public SqlConsumer(ObjectMapper objectMapper,
                       NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                       SqlConsumerProperties properties) {
        this.objectMapper = objectMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sql = properties.getSql();
    }

    @SneakyThrows
    @Override
    public void accept(String payload) {

        Map<String,Object> map = objectMapper.readValue(payload,Map.class);
        map.put("payload",payload);
        namedParameterJdbcTemplate.update(sql,map);
    }
}
