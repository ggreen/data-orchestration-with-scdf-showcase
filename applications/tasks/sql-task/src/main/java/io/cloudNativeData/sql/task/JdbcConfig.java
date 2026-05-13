package io.cloudNativeData.sql.task;

import io.cloudNativeData.sql.task.properties.DbProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableTask
public class JdbcConfig {


    @Bean
    public JdbcTemplate simpleJdbcCall(DbProperties properties) {
        return new JdbcTemplate(DataSourceBuilder.create()
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build());
    }
}
