package showcase.ai.data.orchestration.scdf.rabbit.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "rabbit.stream.sink")
public class RabbitStreamSinkProperties {
    /**
     * The name of the RabbitMQ Stream to which messages will be sent.
     */
    private String streamName;
}
