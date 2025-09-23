package uk.co.danielbryant.djshopping.shopfront;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = ShopfrontApplication.class)
@TestPropertySource("classpath:application-test.properties")
public class ShopfrontApplicationIT {

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context loads successfully
        // Dummy properties from application-test.properties prevent failures in CI
    }
}