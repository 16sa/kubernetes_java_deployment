package uk.co.danielbryant.djshopping.shopfront;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopfrontApplication.class)
@TestPropertySource("classpath:application-test.properties")
public class ShopfrontApplicationIT {

    @Test
    public void contextLoads() {
        // This test ensures that the Spring application context loads successfully
        // Dummy properties from application-test.properties prevent failures in CI
    }
}
