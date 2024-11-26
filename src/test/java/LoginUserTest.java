import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class LoginUserTest extends BaseURI{

    String bearerToken;
    String email = "test12test@yandex.ru";
    String password = "123";
    String name = "testname";

    private final UserSteps user = new UserSteps();

    @After
    public void tearDown() {
        if (bearerToken != null) {
            user.deleteUser(bearerToken);
        }
    }

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    public void testSuccessLoginExistingUser(){

        user.createUser(email, password, name);
        Response response = user.loginUser(email, password);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));

        bearerToken = response.jsonPath().getString("accessToken");

    }

    @Test
    @DisplayName("Ошибка при авторизации с неверным email")
    public void testAuthorizationWithInvalidEmail() {

        user.createUser(email, password, name);

        Response response = user.loginUser("invalidEmail", password);

        bearerToken = response.jsonPath().getString("accessToken");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации с неверным password")
    public void testAuthorizationWithInvalidPassword() {

        user.createUser(email, password, name);

        Response response = user.loginUser(email, "invalidPassword");

        bearerToken = response.jsonPath().getString("accessToken");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации без email")
    public void testAuthorizationWithoutEmail() {

        user.createUser(email, password, name);

        Response response = user.loginUser(null, password);

        bearerToken = response.jsonPath().getString("accessToken");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации без password")
    public void testAuthorizationWithoutPassword() {

        user.createUser(email, password, name);

        Response response = user.loginUser(email, null);

        bearerToken = response.jsonPath().getString("accessToken");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

}
