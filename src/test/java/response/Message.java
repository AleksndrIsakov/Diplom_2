package response;

import lombok.Data;
import static org.junit.Assert.assertEquals;

@Data
public class Message {
    private boolean success;
    private String message;

    public void check(String message, boolean state) {
         assertEquals("Отличается результат выполнения операции", state, success);
         assertEquals("Отличается сообщение по операции", message, this.message);
    }
}
