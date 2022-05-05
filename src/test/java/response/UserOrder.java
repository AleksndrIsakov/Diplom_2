package response;

import lombok.Data;

import java.util.List;

@Data
public class UserOrder {
    private boolean success;
    private List<OrderInfo> orders;
    private String total;
    private String totalToday;

    class OrderInfo {
        String _id;
        List<String> ingredients;
        String status;
        String name;
        String createdAt;
        String updatedAt;
        int number;
    }
}
