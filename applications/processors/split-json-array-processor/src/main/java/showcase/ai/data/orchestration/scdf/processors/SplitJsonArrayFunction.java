package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author gregoryg green
 */
@Component
@Slf4j
public class SplitJsonArrayFunction implements Function<String, List<String>>  {

    private final ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    @Override
    public List<String> apply(String input) {


      log.info("input: {}",input);

        if (input == null || input.isEmpty() || input.trim().isEmpty())
            return null;

        var rootNode = mapper.readTree(input);


        //If not array, return single element
       if(!rootNode.isArray())
            return List.of(rootNode.toString());

       var list = new ArrayList<String>();
       for (var element : rootNode){
            list.add(element.toString());
       }
       return list;
    }
}
