package showcase.ai.data.orchestration.scdf.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    private String email;
    private String firstName;
    private String lastName;
    private Contact contact;
    private Location location;
}
