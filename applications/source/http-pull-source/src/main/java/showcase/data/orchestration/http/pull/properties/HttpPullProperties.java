package showcase.data.orchestration.http.pull.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

/**
 * HttpPullProperties the HTTP pull source properties
 *
 * @author Gregory Green
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "http.pull.source")
public class HttpPullProperties {
    /**
     * The URL to authenticate against.
     */
    private URI authenticateUrlSecret;

    /**
     * The authentication parameter, such as an API key or token.
     */
    private String authenticateUrlParamSecret;

    /**
     * The URL from which to pull data.
     */
    private  URI pullUrlSecret;
}
