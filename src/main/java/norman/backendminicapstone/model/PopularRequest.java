package norman.backendminicapstone.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopularRequest {

    private String productName;
    private float price;
    private String type;
}