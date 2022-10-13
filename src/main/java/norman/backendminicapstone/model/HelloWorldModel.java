package norman.backendminicapstone.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloWorldModel {
    private String firstName;
    private String lastName;
}
