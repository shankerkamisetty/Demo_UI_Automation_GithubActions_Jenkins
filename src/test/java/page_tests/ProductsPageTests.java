package page_tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import page_objects.LoginPageObject;
import page_objects.ProductsPageObject;

public class ProductsPageTests extends BaseTest {
    private static final Logger logger = LogManager.getLogger(ProductsPageTests.class);
    LoginPageObject loginPageObject;
    ProductsPageObject productsPageObject;

    @Test
    public void testItemName() {
        String username = "performance_glitch_user";
        String password = "secret_sauce";
        loginPageObject = new LoginPageObject(driver);
        productsPageObject = loginPageObject.userLogin("performance_glitch_user", "secret_sauce");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Username is: " + username + "Password is: " + password);
        System.out.println(productsPageObject.getTitleOfPage());
        System.out.println(productsPageObject.getItemName());
    }

}
