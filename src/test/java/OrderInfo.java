import lombok.Data;

@Data
public class OrderInfo {
    private boolean success;
    private String name;
    private Order order;
}
