package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author gregoryg green
 */
@Slf4j
@Component
public class SplitJsonArrayProcessor implements Function<byte[], List<Message<byte[]>>>  {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public List<Message<byte[]>> apply(byte[] inputBytes) {

        if (inputBytes == null || inputBytes.length == 0)
            return null;

        var input = new String(inputBytes);
        log.info("input: {}",input);

        if(input.trim().isEmpty())
            return null;

        var rootNode = mapper.readTree(input);


        //If not array, return single element
       if(!rootNode.isArray())
            return List.of(MessageBuilder.withPayload(
                    rootNode.toString().getBytes(StandardCharsets.UTF_8))
                    .build());

       var list = new ArrayList<Message<byte[]>>();
       for (var element : rootNode){
            list.add(MessageBuilder
                    .withPayload(element.toString()
                            .getBytes(StandardCharsets.UTF_8)).build());
       }
       return list;
    }
}
