package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import showcase.ai.data.orchestration.scdf.properties.QueryProperties;

import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueryFunctionProcessor implements Function<String,String> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ObjectMapper objectMapper;
    private final QueryProperties queryProperties;

    @SneakyThrows
    @Override
    public String apply(String payload) {
        var map = objectMapper.readValue(payload, Map.class);


        var inputMap  = objectMapper.readValue(payload, Map.class);
        inputMap.put("payload",payload);

        log.info("SQL: {}, input: {}",queryProperties,inputMap);

        var outMap = namedParameterJdbcTemplate.queryForMap(queryProperties.getSql(),
                inputMap);
        log.info("SQL: {}, results: {}",queryProperties,outMap);

        if(outMap.size() == 1 && outMap.containsKey("payload")) {
            var outPayload = String.valueOf(outMap.get("payload"));
            log.info("Returning payload: {}",outPayload);
            return outPayload;
        }

        var out = objectMapper.writeValueAsString(outMap);
        log.info("Returning: {}",out);
        return out;
    }
}
