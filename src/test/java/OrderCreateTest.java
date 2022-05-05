import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import request.Ingredient;
import response.Message;
import response.OrderList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private List<Ingredient> ingredients;
    private int expectedStatusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userClient.register(UserGenerator.random());
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        userClient.delete();
    }

    public OrderCreateTest(List<Ingredient> ingredients, int statusCode) {
        this.ingredients = ingredients;
        this.expectedStatusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] setData() {
        List<Ingredient> ingredients = OrderClient.getIngredients().data;
        List<String> uniqueType = ingredients.stream().map(Ingredient::getType).distinct().collect(Collectors.toList());

        // Набор уникальных по типу ингредиентов
        List<Ingredient> uniqueIngredients = new ArrayList<>();
        for (String type : uniqueType)
            uniqueIngredients.add(ingredients.stream().filter(i -> i.getType().equals(type)).findAny().get());

        // Набор ингредиентов с неверным хеш кодом
        List<Ingredient> wrongHashIngredients = new ArrayList<>();
        wrongHashIngredients.add(Ingredient.builder()._id(RandomStringUtils.randomAlphabetic(5)).build());

        // Пустой набор ингредиентов
        List<Ingredient> withoutIngredients = new ArrayList<>();

        return new Object[][]{
                {uniqueIngredients, SC_OK},
                {withoutIngredients, SC_BAD_REQUEST},
                {wrongHashIngredients, SC_INTERNAL_SERVER_ERROR}
        };
    }

    @Test
    @DisplayName("Создание заказа c авторизацией")
    public void checkCreateOrder() {
        ValidatableResponse response = orderClient.createOrder(userClient, ingredients);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(expectedStatusCode));

        if (expectedStatusCode == SC_OK) {
            OrderList orderInfo = response.extract().as(OrderList.class);
        }

        if (expectedStatusCode == SC_BAD_REQUEST) {
            Message message = response.extract().as(Message.class);
            message.check("Ingredient ids must be provided", false);
        }

    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void checkCreateOrderWithoutAuthorize() {
        ValidatableResponse response = orderClient.createOrder(ingredients);
        int statusCode = response.extract().statusCode();

        assertThat(statusCode, equalTo(expectedStatusCode));

        if (expectedStatusCode == SC_BAD_REQUEST) {
            Message message = response.extract().as(Message.class);
            message.check("Ingredient ids must be provided", false);
        }
    }

    private void checkExpectedBody(ValidatableResponse response) {

    }
}
