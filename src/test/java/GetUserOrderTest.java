import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class GetUserOrderTest extends BaseURI{

    String bearerToken;
    String email = "lala123lal54@yandex.ru";
    String password = "123";
    String name = "test";
    private final UserSteps user = new UserSteps();
    private final OrderSteps order = new OrderSteps();

    @After
    public void tearDown() {
        if (bearerToken != null) {
            user.deleteUser(bearerToken);
        }
    }

    @Test
    @DisplayName("Получение заказов определенного пользователя с авторизацией")
    public void testGetUserOrdersAuthorized(){
        user.createUser(email, password, name);
        Response loginResponse = user.loginUser(email, password);
        bearerToken = loginResponse.jsonPath().getString("accessToken");

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"};

        order.makeOrder(ingredients, bearerToken);

        Response ordersResponse = order.getUserOrder(bearerToken);

        ordersResponse.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders[0].ingredients", notNullValue())
                 .body("orders[0]._id", notNullValue())
                .body("orders[0].status", notNullValue())
                .body("orders[0].number", notNullValue())
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue())
                .body("total", greaterThan(0))
                .body("totalToday", greaterThan(0));
    }

    @Test
    @DisplayName("Получение заказов определенного пользователя без авторизации")
    public void testGetUserOrdersUnauthorized(){

        user.createUser(email, password, name);
        Response loginResponse = user.loginUser(email, password);
        bearerToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"};
        order.makeOrder(ingredients, bearerToken);

        user.userLogout(refreshToken);

        Response response = order.getUserOrder(bearerToken);

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
