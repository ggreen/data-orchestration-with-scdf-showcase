package showcase.ai.data.orchestration.scdf.rabbit.sink;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StreamSinkTest {

    private StreamSink subject;

    @Mock
    private RabbitStreamTemplate template;
    private final static String payload = "Helloworld";


    @BeforeEach
    void setUp() {
        subject = new StreamSink(template);
    }

    @Test
    void publishToStream() {
        subject.accept(payload);


         verify(template).convertAndSend(anyString());

    }
}