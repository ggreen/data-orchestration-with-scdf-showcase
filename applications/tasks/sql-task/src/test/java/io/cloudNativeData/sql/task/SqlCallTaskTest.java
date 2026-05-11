package io.cloudNativeData.sql.task;

import io.cloudNativeData.sql.task.properties.DbProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SqlCallTaskTest {

    @Mock
    private ApplicationArguments args;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DbProperties properties;

    private CallTask subject;
    private String sql = "call my stored procedure";

    @BeforeEach
    void setUp() {
        subject = new CallTask(jdbcTemplate,properties);
    }

    @Test
    void given_args_when_run_then_call_exe() throws Exception {
        when(properties.getSql()).thenReturn(sql);
        subject.run(args);

        verify(jdbcTemplate).execute(anyString());
    }
}