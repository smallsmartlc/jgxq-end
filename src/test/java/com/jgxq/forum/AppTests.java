package com.jgxq.forum;

import com.jgxq.common.utils.PasswordHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SpringBootTest
class AppTests {

    @Test
    void HashTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "1111111";
        String daoPwd = "1111111";
        System.out.println(PasswordHash.validatePassword(password,daoPwd));
    }

    @Test
    void contextLoads() {
    }

}
