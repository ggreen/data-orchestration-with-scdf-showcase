package showcase.ai.data.orchestration.scdf.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private Contact contact;
    private Location location;
}
