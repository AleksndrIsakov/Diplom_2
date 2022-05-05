import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.Ingredient;
import response.Message;
import response.UserOrder;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Получение заказов")
public class OrderGetTest {

    private UserClient userClient;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userClient.register(UserGenerator.random());

        orderClient = new OrderClient();
        List<Ingredient> ingredients = OrderClient.getIngredients().data;
        orderClient.createOrder(userClient, ingredients);
    }

    @After
    public void tearDown() {
        userClient.delete();
    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    public void getOrdersForAuthorizedUser() {
        ValidatableResponse response = orderClient.getOrders(userClient);
        int statusCode = response.extract().statusCode();
        UserOrder orders = response.extract().as(UserOrder.class);

        assertThat(statusCode, equalTo(SC_OK));
        assertThat(orders.isSuccess(), equalTo(true));
    }

    @Test
    @DisplayName("Получение заказа неавторизованным пользователем")
    public void getOrdersForUnAuthorizedUser() {
        ValidatableResponse response = orderClient.getOrders();
        int statusCode = response.extract().statusCode();
        Message message = response.extract().as(Message.class);

        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
        message.check("You should be authorised", false);
    }
}
