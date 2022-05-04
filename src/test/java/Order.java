import lombok.Data;
import java.util.List;

@Data
public class Order {
    private String _id;
    private List<Ingredient> ingredients;
    private Owner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;
}
