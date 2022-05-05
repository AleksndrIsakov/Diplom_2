import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import request.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class UserClient extends StellarBurgersRestClient {

    private static final String REGISTER = "api/auth/register";
    private static final String LOGIN = "api/auth/login";
    private static final String USER = "api/auth/user";

    private String accessToken;
    private String refreshToken;

    @Step("Создание пользователя")
    public ValidatableResponse register(User user) {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(REGISTER)
                .then();

        getTokens(response);
        return response;
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user) {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(LOGIN)
                .then();

        getTokens(response);
        return response;
    }

    @Step("Получение информации о пользователе")
    public ValidatableResponse getInfo() {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(USER)
                .then();
    }

    @Step("Обновление информации о пользователе")
    public ValidatableResponse updateInfo(User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .when()
                .patch(USER)
                .then();
    }

    @Step("Обновление информации о пользователе без авторизации")
    public ValidatableResponse updateInfoWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete() {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER)
                .then();
    }

    public String getAuthToken() {
        return accessToken;
    }

    private void getTokens(ValidatableResponse response) {
        if (response.extract().statusCode() == SC_OK) {
            accessToken = response.extract().jsonPath().getString("accessToken").replace("Bearer ", "");
            refreshToken = response.extract().jsonPath().getString("refreshToken").replace("Bearer ", "");
        }
    }
}
