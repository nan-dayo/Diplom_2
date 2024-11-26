import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class UserSteps {

    String CREATE_USER_URL = "/api/auth/register";
    String LOGIN_USER_URL = "/api/auth/login";
    String CHANGE_USER_DATA_URL = "/api/auth/user";
    String LOGOUT_USER_URL = "/api/auth/logout";

    @Step("Создать пользователя с параметрами: email={email}, password={password}, name={name}")
    public Response createUser(String email, String password, String name){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("name", name);

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(CREATE_USER_URL);
    }

    @Step("Логин пользователя с параметрами: email={email}, password={password}")
    public Response loginUser(String email, String password){
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        return given()
                .header("content-Type", "application/json")
                .body(loginData)
                .when()
                .post(LOGIN_USER_URL);
    }

    @Step("Изменить данные пользователя")
    public Response changeData(String email, String name, String bearerToken){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("name", name);

        return given()
                .headers("Content-type", "application/json", "Authorization", bearerToken)
                .log().all()
                .body(requestBody)
                .when()
                .patch(CHANGE_USER_DATA_URL);

    }

    @Step("Выйти из системы")
    public Response userLogout(String refreshToken){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("token", refreshToken);
        return given()
                .header("content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(LOGOUT_USER_URL);
    }


    @Step("Удалить пользователя")
    public void deleteUser(String bearerToken){
        Response response =
                given()
                        .headers("Content-type", "application/json", "Authorization", bearerToken)
                        .log().all()
                        .when()
                        .delete(CHANGE_USER_DATA_URL);
        response.then().statusCode(202);
    }


}
