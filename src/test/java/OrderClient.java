import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import request.Ingredient;
import request.Ingredients;

import java.util.List;
import java.util.stream.Collectors;

public class OrderClient extends StellarBurgersRestClient {

    private static final String INGREDIENTS = "api/ingredients";
    private static final String ALL_ORDERS = "api/orders/all";
    private static final String USER_ORDERS = "api/orders";

    @Step("Получение доступных ингредиентов")
    public static ListOfIngredient getIngredients() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS)
                .then()
                .extract().as(ListOfIngredient.class);
    }

    @Step("Создание заказа пользователя с авторизацией")
    public ValidatableResponse createOrder(UserClient client, List<Ingredient> ingredients) {
        Ingredients listToOrder = new Ingredients();
        listToOrder.setIngredients(ingredients.stream().map(Ingredient::get_id).collect(Collectors.toList()));

        return RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(client.getAuthToken())
                .body(listToOrder)
                .when()
                .post(USER_ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrder(List<Ingredient> ingredients) {
        Ingredients listToOrder = new Ingredients();
        listToOrder.setIngredients(ingredients.stream().map(Ingredient::get_id).collect(Collectors.toList()));

        return RestAssured.given()
                .spec(getBaseSpec())
                .body(listToOrder)
                .when()
                .post(USER_ORDERS)
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getOrders(UserClient client) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(client.getAuthToken())
                .when()
                .get(USER_ORDERS)
                .then();
    }

    @Step("Получение заказов без авторизации")
    public ValidatableResponse getOrders() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get(USER_ORDERS)
                .then();
    }

    class ListOfIngredient {
        Boolean success;
        List<Ingredient> data;
    }
}
