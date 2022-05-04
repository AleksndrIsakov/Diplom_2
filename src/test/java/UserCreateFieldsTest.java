import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class UserCreateFieldsTest {

    private UserClient client;
    private User user;

    public UserCreateFieldsTest(Field field) {
        this.user = UserGenerator.random();
        switch (field) {
            case NAME: user.setName("");
                break;
            case EMAIL: user.setEmail("");
                break;
            case PASSWORD: user.setPassword("");
                break;
        }
    }

    @Before
    public void setUp() {
        client = new UserClient();
    }

    @Parameterized.Parameters
    public static Object[][] setData() {
        return new Object[][]{
                {Field.NAME},
                {Field.EMAIL},
                {Field.PASSWORD}
        };
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным обязательным полем")
    public void createWithUnfilledFields() {
        ValidatableResponse response = client.register(user);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }

    enum Field {
        NAME,
        EMAIL,
        PASSWORD
    }
}


