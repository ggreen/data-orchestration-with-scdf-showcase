package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class SplitJsonArrayProcessor implements Function<String, List<Message<String>>> {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public List<Message<String>> apply(String input) {

        if (input == null || input.isEmpty() || input.trim().isEmpty())
            return null;

        JsonNode rootNode = mapper.readTree(input);

        var messages = new ArrayList<Message<String>>();

        for (JsonNode element : rootNode) {
            messages.add(MessageBuilder.withPayload(element.toString()).build());
        }

        return messages;
    }
}
