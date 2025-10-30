package showcase.ai.data.orchestration.scdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import reactor.core.publisher.Flux;
import showcase.ai.data.orchestration.scdf.processors.SplitJsonArrayFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Configuration
public class RabbitMQConfig {

    @Value("${spring.cloud.stream.bindings.output.destination:output}")
    private String exchange;



    @Bean
    AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory)
    {
        var template = new RabbitTemplate(connectionFactory);
        template.setExchange(exchange);
        return template;
    }


    @Bean
    Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(exchange)
                .durable(true)    // persists after broker restart
                .build();
    }


    @Bean
    public Function<String, List<Message<String>>> reactiveUpperCase() {
        return p -> {
            List<Message<String>> list = new ArrayList<>();
            list.add(MessageBuilder.withPayload(p + ":1").build());
            list.add(MessageBuilder.withPayload(p + ":2").build());
            list.add(MessageBuilder.withPayload(p + ":3").build());
            list.add(MessageBuilder.withPayload(p + ":4").build());
            return list;
        };
    }


    @Bean
    Consumer<String> splitJsonArrayProcessor(SplitJsonArrayFunction splitJsonArrayFunction, AmqpTemplate amqpTemplate)
    {
        return input -> {
            var list = splitJsonArrayFunction.apply(input);

            if(list == null || list.isEmpty())
                return;

            list.forEach(amqpTemplate::convertAndSend);
        };
    }
//    @Bean
//    @Primary
//    MessageConverter convert(ObjectMapper objectMapper){
//       return  new MessageConverter() {
//           @Override
//           public Object fromMessage(Message<?> message, Class<?> targetClass) {
//               log.info("from");
//               return null;
//           }
//
//           @Override
//           public Message<?> toMessage(Object payload, MessageHeaders headers) {
//               log.info("to");
//               return null;
//           }
//       };
//    }
}
