import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class ChangeUserDataTest extends BaseURI{

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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void authorizedChangeUserData(){
        String editedEmail = "editedemail123@yandex.ru";
        String editedName = "editedname";

        user.createUser(email, password, name);
        Response response1 = user.loginUser(email, password);

        bearerToken = response1.jsonPath().getString("accessToken");

        Response response2 = user.changeData(editedEmail, editedName, bearerToken);
        response2.then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(editedEmail))
                .body("user.name", equalTo(editedName));

    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void unauthorizedChangeUserData(){
        String editedEmail = "editedemail123@yandex.ru";
        String editedName = "editedname";

        Response response1 = user.createUser(email, password, name);
        bearerToken = response1.jsonPath().getString("accessToken");

        Response response2 = user.changeData(editedEmail, editedName, bearerToken);
        response2.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
