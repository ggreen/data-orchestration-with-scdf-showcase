package showcase.ai.data.orchestration.scdf.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitSinkApp {
    public static void main(String[] args) {
        SpringApplication.run(RabbitSinkApp.class,args);
    }
}
