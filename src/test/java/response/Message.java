package response;

import lombok.Data;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@Data
public class Message implements CanCheck {
    private boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;

    @Override
    public void check(Object expected, boolean state) {
        assertEquals("Отличается результат выполнения операции", state, success);
        if (expected instanceof String)
            assertEquals("Отличается сообщение по операции", expected, this.message);

        if (expected instanceof User) {
            assertEquals("Отличается name", ((User) expected).getName(), user.getName());
            assertEquals("Отличается email", ((User) expected).getEmail().toLowerCase(Locale.ROOT), user.getEmail());
        }
    }
}
