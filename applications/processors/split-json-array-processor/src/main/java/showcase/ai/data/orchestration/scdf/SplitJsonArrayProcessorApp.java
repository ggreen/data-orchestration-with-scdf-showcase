package showcase.ai.data.orchestration.scdf;

import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.util.Debugger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SplitJsonArrayProcessorApp {

	public static void main(String[] args) {
		log.info("SplitJsonArrayProcessorApp.main PROPERTIES:"+System.getProperties()+" args:"+ Debugger.toString(args));
		SpringApplication.run(SplitJsonArrayProcessorApp.class, args);
	}

}
