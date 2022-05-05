import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import request.User;
import response.Message;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Изменение данных пользователя")
@RunWith(Parameterized.class)
public class UserUpdateTest {

    private UserClient client;
    private User user;

    public UserUpdateTest(Field field) {
        user = UserGenerator.random();
        switch (field) {
            case NAME:
                user.setName(RandomStringUtils.randomAlphabetic(10));
                break;
            case EMAIL:
                user.setEmail(UserGenerator.randomEmail());
                break;
            case PASSWORD:
                user.setPassword(RandomStringUtils.randomAlphabetic(10));
                break;
            default: // Ничего не меняем
                break;
        }

    }

    @Before
    public void setUp() {
        client = new UserClient();
        client.register(user);
    }

    @After
    public void tearDown() {
        client.delete();
    }

    @Parameterized.Parameters
    public static Object[][] setParams() {
        return new Object[][]{
                {Field.NAME},
                {Field.EMAIL},
                {Field.PASSWORD},
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void updateUserWithAuthorization() {
        ValidatableResponse response = client.updateInfo(user);
        int statusCode = response.extract().statusCode();
        Message message = response.extract().as(Message.class);

        assertThat(statusCode, equalTo(SC_OK));
        message.check(user.getName(), user.getEmail(), true);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void updateUserWithoutAuthorization() {
        ValidatableResponse response = client.updateInfoWithoutAuth(user);
        int statusCode = response.extract().statusCode();
        Message message = response.extract().as(Message.class);

        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
        message.check("You should be authorised", false);
    }

    enum Field {
        NAME,
        EMAIL,
        PASSWORD
    }
}
