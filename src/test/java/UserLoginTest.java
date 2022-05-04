import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Создание пользователя")
public class UserLoginTest {

    private UserClient client;
    private User user;

    @Before
    public void setUp() {
        client = new UserClient();
        user = UserGenerator.random();
        client.register(user);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginSuccessful() {
        ValidatableResponse response = client.login(user);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Авторизация c неверным логином")
    public void loginWithWrongEmail() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = client.login(user);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginWithWrongPassword() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = client.login(user);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }

    public void tearDown() {
        client.delete();
    }
}
