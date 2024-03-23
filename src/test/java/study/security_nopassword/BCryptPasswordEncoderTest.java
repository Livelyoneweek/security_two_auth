package study.security_nopassword;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {

    @Test
    @DisplayName("암호화")
    void getEncryptNumber() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "151315";
        String encodedPassword = encoder.encode(password);
        System.out.println("암호화된 비밀번호: " + encodedPassword);
    }
}
