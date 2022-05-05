package response;

import lombok.Data;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@Data
public class Message {
    private boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;

    public void check(String message, boolean state) {
         assertEquals("Отличается результат выполнения операции", state, success);
         assertEquals("Отличается сообщение по операции", message, this.message);
    }

    public void check(String name, String email, boolean state) {
        assertEquals("Отличается результат выполнения операции", state, success);
        assertEquals("Отличается name", name, user.getName());
        assertEquals("Отличается email", email.toLowerCase(Locale.ROOT), user.getEmail());
    }
}
