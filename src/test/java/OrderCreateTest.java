import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderCreateTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private List<Ingredient> ingredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        userClient.register(UserGenerator.random());
        ingredients = OrderClient.getIngredients().data;
    }

    @After
    public void tearDown() {
        userClient.delete();
    }

    @Test
    @DisplayName("Создание заказа c авторизацией")
    public void createOrder() {
        List<Ingredient> listToOrder = new ArrayList<>();
        List<String> uniqueType = ingredients.stream().map(Ingredient::getType).distinct().collect(Collectors.toList());

        for (String type: uniqueType)
            listToOrder.add(ingredients.stream().filter(i->i.getType().equals(type)).findAny().get());

        ValidatableResponse response = orderClient.createOrder(userClient, listToOrder);
        int statusCode = response.extract().statusCode();

        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorize() {
        List<Ingredient> listToOrder = new ArrayList<>();
        listToOrder.add(Ingredient.builder()._id("61c0c5a71d1f82001bdaaa6d").build());
        listToOrder.add(Ingredient.builder()._id("61c0c5a71d1f82001bdaaa6f").build());

        ValidatableResponse response = orderClient.createOrder(listToOrder);
        int statusCode = response.extract().statusCode();

        assertThat(statusCode, equalTo(SC_OK));
    }
}
