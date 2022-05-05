package response;

import lombok.Data;

import java.util.Calendar;

@Data
public class Owner {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
}
