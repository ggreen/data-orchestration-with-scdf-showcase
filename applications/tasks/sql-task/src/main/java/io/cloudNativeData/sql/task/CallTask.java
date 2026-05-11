package io.cloudNativeData.sql.task;

import io.cloudNativeData.sql.task.properties.DbProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CallTask implements ApplicationRunner {

    private final JdbcTemplate call;
    private final DbProperties dbProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        call.execute(dbProperties.getSql());
    }
}
