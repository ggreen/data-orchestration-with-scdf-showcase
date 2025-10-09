package showcase.ai.data.orchestration.scdf.rabbit.sink;

import lombok.RequiredArgsConstructor;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * A simple RabbitMQ Stream sink that sends incoming String messages to a RabbitMQ Stream.
 * @author gregory green
 */
@Component
@RequiredArgsConstructor
public class StreamSink implements Consumer<String> {
    private final RabbitStreamTemplate template;

    /**
     * Publish the given payload to the RabbitMQ Stream.
     * @param payload the input argument
     */
    @Override
    public void accept(String payload) {
        template.convertAndSend(payload);
    }
}
