package showcase.ai.data.orchestration.scdf.rabbit;

import com.rabbitmq.stream.Environment;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.rabbit.stream.support.StreamAdmin;
import showcase.ai.data.orchestration.scdf.rabbit.properties.RabbitStreamSinkProperties;

@Configuration
@EnableConfigurationProperties(RabbitStreamSinkProperties.class)
public class RabbitConfig {

    @Bean
    StreamAdmin streamAdmin(Environment env, RabbitStreamSinkProperties rabbitStreamSinkProperties) {
        return new StreamAdmin(env, sc -> {
            sc.stream(rabbitStreamSinkProperties.getStreamName()).create();
        });
    }

    @Bean
    RabbitStreamTemplate streamTemplate(Environment env, RabbitStreamSinkProperties rabbitStreamSinkProperties) {
        return new RabbitStreamTemplate(env, rabbitStreamSinkProperties.getStreamName());
    }
}
