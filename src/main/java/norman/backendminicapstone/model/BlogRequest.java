package norman.backendminicapstone.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogRequest {

    private String blogName;
    private String blogAuthor;
    private String description;
}