import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import request.User;
import response.Message;
import response.UserMessage;

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
        UserMessage message = response.extract().as(UserMessage.class);

        assertThat(statusCode, equalTo(SC_OK));
        message.check(user.getName(), user.getEmail(), true);
    }

    @Test
    @DisplayName("Авторизация c неверным логином")
    public void loginWithWrongEmail() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = client.login(user);
        int statusCode = response.extract().statusCode();
        Message message = response.extract().as(Message.class);

        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
        message.check("email or password are incorrect", false);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginWithWrongPassword() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = client.login(user);
        int statusCode = response.extract().statusCode();
        Message message = response.extract().as(Message.class);

        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
        message.check("email or password are incorrect", false);
    }

    public void tearDown() {
        client.delete();
    }
}
