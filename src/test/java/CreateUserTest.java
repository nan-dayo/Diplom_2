import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateUserTest extends BaseURI {

    String bearerToken;

    private final UserSteps user = new UserSteps();

    @After
    public void tearDown() {
        if (bearerToken != null) {
            user.deleteUser(bearerToken);
        }
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    public void testCreateUserSuccess() {
        String email = "test12test@yandex.ru";
        String password = "123";
        String name = "testname";

        Response response = user.createUser(email, password, name);
        response.then().statusCode(200).body("success", is(true));

        bearerToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Ошибка при создании пользователя, который уже зарегистрирован")
    public void testAlreadyExistingUser() {
        String email = "tttttest12test@yandex.ru";
        String password = "123";
        String name = "testname";

        Response response = user.createUser(email, password, name);
        response.then().statusCode(200).body("success", is(true));

        bearerToken = response.jsonPath().getString("accessToken");

        response = user.createUser(email, password, name);
        response.then().statusCode(403)
                .body("message", equalTo("User already exists"));
    }


    @Test
    @DisplayName("Ошибка при создании пользователя без обязательного поля email")
    public void testCreateUserWithoutEmail() {
        String email = null;
        String password = "123";
        String name = "testname";

        Response response = user.createUser(email, password, name);

        response.then().statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));

        bearerToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без обязательного поля password")
        public void testCreateUserWithoutPassword() {
            String email = "tetetest12test@yandex.ru";
            String password = null;
            String name = "testname";

            Response response = user.createUser(email, password, name);

            response.then().statusCode(403)
                    .body("message", equalTo("Email, password and name are required fields"));

            bearerToken = response.jsonPath().getString("accessToken");

    }

    @Test
    @DisplayName("Ошибка при создании пользователя без обязательного поля name")
    public void testCreateUserWithoutName() {
        String email = "tetetest12test@yandex.ru";
        String password = "123";
        String name = null;

        Response response = user.createUser(email, password, name);

        response.then().statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));

        bearerToken = response.jsonPath().getString("accessToken");

    }
}
