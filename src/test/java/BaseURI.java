import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class BaseURI {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

}
