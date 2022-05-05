package request;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

@Builder
@Data
public class User {

    private String email;
    private String password;
    private String name;

}
