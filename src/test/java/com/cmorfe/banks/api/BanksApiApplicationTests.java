package com.cmorfe.banks.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BanksApiApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> BanksApiApplication.main(new String[]{}));
    }
}
