import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.*;

public class Registration {

    private WebDriver driver;
    private String email;
    private String password;

    @Before
    public void setDriver() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Kamil/IdeaProjects/chromedriver.exe");  //for Linux, delete .exe from chromedriver and change relative path
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void registerIfAccountDoesNotExist() {
        //generation of fake email and password
        driver.get("https://www.fakenamegenerator.com/");

        Select genderDropDown = new Select(driver.findElement(By.id("gen")));
        genderDropDown.selectByVisibleText("Random");
        Select nameSetDropDown = new Select(driver.findElement(By.id("n")));
        nameSetDropDown.selectByVisibleText("Polish");
        Select countryDropDown = new Select(driver.findElement(By.id("c")));
        countryDropDown.selectByVisibleText("Poland");

        driver.findElement(By.id("genbtn")).click();

        WebElement generatedEmail = driver.findElement(By.xpath("//*[@id=\"details\"]/div[2]/div[2]/div/div[2]/dl[9]/dd"));
        String fullEmail = generatedEmail.getText();
        email = fullEmail.substring(0, fullEmail.indexOf("\n"));
        System.out.println(email);

        WebElement generatedPassword = driver.findElement(By.xpath("//*[@id=\"details\"]/div[2]/div[2]/div/div[2]/dl[11]/dd"));
        password = generatedPassword.getText();
        System.out.println(password);

        //login trial and if not succedd, registration with email and password from generator above
        driver.get("https://www.aptekagemini.pl/user/loginUser");
        WebElement userEmail = driver.findElement(By.name("user[email]"));
        if (userEmail.isDisplayed()) {
            userEmail.sendKeys(email);
        } else
            fail();

        WebElement userPassword = driver.findElement(By.name("user[password]"));
        if (userPassword.isDisplayed()) {
            userPassword.sendKeys(password);
        } else
            fail();

        driver.findElement(By.id("st_button-user-login_success")).click();

        WebElement errorMassage = driver.findElement(By.xpath("//*[@id=\"login-user\"]/form/fieldset/div[1]/div[1]/img"));
        if (errorMassage.getAttribute("data-tooltip").equals("Zły login lub hasło.") || errorMassage.getAttribute("data-tooltip").equals("Brak hasła.")) {
            WebElement register = driver.findElement(By.xpath("//*[@id=\"st_application-user-login-register\"]/div/div/div[1]/a"));
            register.click();
        } else
            fail();

        driver.findElement(By.id("kukiz_x")).click();

        WebElement newUserEmail = driver.findElement(By.name("user[email]"));
        if (newUserEmail.isDisplayed()) {
            newUserEmail.sendKeys(email);
        } else
            fail();

        WebElement newUserPassword = driver.findElement(By.name("user[password1]"));
        if (newUserPassword.isDisplayed()) {
            newUserPassword.sendKeys(password);
        } else
            fail();

        WebElement newUserPassword2 = driver.findElement(By.id("st_form-user-password2"));
        if (newUserPassword2.isDisplayed()) {
            newUserPassword2.sendKeys(password);
        } else
            fail();

        WebElement acceptCheckbox = driver.findElement(By.className("checkbox"));
        if (acceptCheckbox.isEnabled()) {
            acceptCheckbox.click();
        } else
            fail();

        driver.findElement(By.id("st_button-user-account")).submit();

        //assertion of registration, but it's still needed to click a link in an email (will be updated in ver 2.0)
        WebElement assertion = driver.findElement(By.xpath("//*[@id=\"st_application-user-account-buttons\"]/div/a/span"));
        assertEquals("Wróć do zakupów", assertion.getText());
    }

    @Test
    public void loginIfAccountExists() {
        // the email an password allow login to log in for already created user
        email = "RenardTomaszewski@dayrep.com";
        password = "thikae4Oof";

        driver.get("https://www.aptekagemini.pl/user/loginUser");
        WebElement userEmail = driver.findElement(By.name("user[email]"));
        if (userEmail.isDisplayed()) {
            userEmail.sendKeys(email);
        } else
            fail();

        WebElement userPassword = driver.findElement(By.name("user[password]"));
        if (userPassword.isDisplayed()) {
            userPassword.sendKeys(password);
        } else
            fail();

        driver.findElement(By.id("kukiz_x")).click();

        driver.findElement(By.id("st_button-user-login_success")).click();

        WebElement emptyBasket = driver.findElement(By.id("empty_basket_text"));
        assertEquals("Kliknij tutaj, aby kontynuować zakupy lub przejdź do Ulubionych.", emptyBasket.getText());
    }
}
