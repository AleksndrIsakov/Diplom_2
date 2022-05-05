package response;
import lombok.Data;
import request.Ingredient;

import java.util.List;

import static org.junit.Assert.*;

@Data
public class OrderList implements CanCheck {
    private boolean success;
    private String name;
    private Order order;

    @Override
    public void check(Object expected, boolean state) {
        assertEquals("Отличается результат выполнения операции", state, success);
        if (expected instanceof OrderList) {
            assertNotNull(name);
            assertNotNull(order);

            List<Ingredient> actualIngredient = order.getIngredients();
            List<Ingredient> expextedIngredient = ((OrderList) expected).getOrder().getIngredients();

            if (actualIngredient != null)
                assertTrue(actualIngredient.stream().allMatch(x->expextedIngredient.contains(x)));
        }
    }
}
