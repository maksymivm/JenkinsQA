import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class FirstTest extends BaseTest {

    @Test
    public void test() {
        getDriver().findElement(By.xpath("//a[@href='/asynchPeople/']")).click();
    }
}
