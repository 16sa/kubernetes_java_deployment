package uk.co.danielbryant.djshopping.shopfront;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShopfrontApplication.class)
@TestPropertySource(properties = "eureka.client.enabled=false")
public class ShopfrontApplicationTests {

    @Test
    public void contextLoads() {
    }

}
