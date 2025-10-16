package showcase.data.orchestration.http.pull.functions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import showcase.data.orchestration.http.pull.properties.HttpPullProperties;

import java.util.List;
import java.util.function.Supplier;

/**
 * A simple HTTP Pull Supplier that authenticates and then pulls data from a configured HTTP endpoint.
 * @author gregory green
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpPullSupplier implements Supplier<String> {

    private final HttpPullProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public String get() {

        // Headers
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Request entity
        var requestEntity = new HttpEntity<String>(properties.getAuthenticateParam(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                properties.getAuthenticateUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        List<String> cookie = null;

        var responseHeaders = response !=null ? response.getHeaders(): null;
        if(responseHeaders != null)
            cookie = responseHeaders.get(HttpHeaders.SET_COOKIE);

        var queryHeaders = new HttpHeaders();

        if(cookie != null)
            queryHeaders.addAll(HttpHeaders.COOKIE,cookie);

        HttpEntity<String> entity = new HttpEntity<>(null, queryHeaders);

        var responseEntity = restTemplate.exchange(
                properties.getPullUrl(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return responseEntity.getBody();
    }
}
