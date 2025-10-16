package showcase.data.orchestration.http.pull.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import showcase.data.orchestration.http.pull.properties.HttpPullProperties;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Gregory Green
 */
@ExtendWith(MockitoExtension.class)
class HttpPullSupplierTest {

    private HttpPullSupplier subject;
    @Mock
    private HttpPullProperties properties;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity response;

    @BeforeEach
    void setUp() {
        subject = new HttpPullSupplier(properties,restTemplate);
    }

    @Test
    void pullData() {

        when(restTemplate.exchange(
                any(URI.class),
                any(),
                any(),
                any(Class.class)
        )).thenReturn(response);

        when(properties.getAuthenticateUrlParamSecret()).thenReturn("param=value");
        when(properties.getAuthenticateUrlSecret()).thenReturn(URI.create("https://postman-echo.com/post"));
        when(properties.getPullUrlSecret()).thenReturn(URI.create("https://postman-echo.com/get?foo1=bar1&foo2=bar2"));
        when(response.getBody()).thenReturn("response");

        var responseText = subject.get();

        assertThat(responseText).isNotEmpty();
    }
}