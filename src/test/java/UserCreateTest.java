import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserCreateTest {

    private UserClient client;
    private User user;

    @Before
    public void setUp() {
        client = new UserClient();
        user = UserGenerator.random();
    }

    @After
    public void tearDown() {
        client.getInfo();
        client.delete();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUser() {

        ValidatableResponse response = client.register(user);
        int statusCode = response.extract().statusCode();
        assertThat("Код ответа отличается от ожидаемого", statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание не уникального пользователя")
    public void createNonUniqueUser() {
        client.register(user);
        ValidatableResponse response = client.register(user);
        int statusCode = response.extract().statusCode();
        assertThat("Код ответа отличается от ожидаемого", statusCode, equalTo(SC_FORBIDDEN));
    }
}
