package io.cloudNativeData.sql.task.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "db")
@Data
public class DbProperties {

        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String sql;
}
