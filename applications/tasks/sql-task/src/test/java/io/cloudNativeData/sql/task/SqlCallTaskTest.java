package io.cloudNativeData.sql.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SqlCallTaskTest {

    @Mock
    private ApplicationArguments args;

    @Mock
    private SimpleJdbcCall call;

    private CallTask subject;

    @BeforeEach
    void setUp() {
        subject = new CallTask(call);
    }

    @Test
    void given_args_when_run_then_call_exe() throws Exception {
        subject.run(args);

        verify(call).execute(any(Map.class));

    }

    @Test
    void toMap() {
        Map<String,String> expected = Map.of("hello", "world");
        when(args.getOptionNames()).thenReturn(Set.of("hello"));
        when(args.getOptionValues(anyString())).thenReturn(List.of("world"));
        var actual = subject.toMap(args);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toMap_empty() {
        assertThat(subject.toMap(args)).isEmpty();
        assertThat(subject.toMap(null)).isEmpty();
    }
}