import org.apache.commons.lang3.RandomStringUtils;
import request.User;

public class UserGenerator {

    public static User random() {
        return User.builder()
                .name(RandomStringUtils.randomAlphabetic(10))
                .password(RandomStringUtils.randomAlphabetic(10))
                .email(randomEmail())
                .build();
    }

    public static String randomEmail() {
        return RandomStringUtils.randomAlphabetic(5) + "@test.com";
    }
}
