package response;

import lombok.Data;

@Data
public class OrderList {
    private boolean success;
    private String name;
    private Order order;
}
