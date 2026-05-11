package io.cloudNativeData.sql.task;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CallTask implements ApplicationRunner {

    private final SimpleJdbcCall call;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        call.execute(toMap(args));
    }

    /**
     *
     * @param args the task input augments
     * @return the map of arguments
     */
    Map<String, ?> toMap(ApplicationArguments args) {

        if(args == null )
            return Collections.emptyMap();

        var optNames = args.getOptionNames();
        if (optNames.isEmpty())
            return Collections.emptyMap();

        Map<String, String> argMap = new HashMap<>();

        optNames.forEach(name -> {
            var opts = args.getOptionValues(name);
            if(!opts.isEmpty())
            {
                argMap.put(name,opts.get(0));
            }
        });
        return argMap;
    }
}
