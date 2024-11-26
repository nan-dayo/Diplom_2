import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest extends BaseURI{

    String bearerToken;
    String email = "234test12@yandex.ru";
    String password = "123";
    String name = "testte";
    private final UserSteps user = new UserSteps();
    private final OrderSteps order = new OrderSteps();

    @After
    public void tearDown() {
        if (bearerToken != null) {
            user.deleteUser(bearerToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void testCrateOrderAuthorized(){
        user.createUser(email, password, name);
        Response response1 = user.loginUser(email, password);
        bearerToken = response1.jsonPath().getString("accessToken");

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72"};

        Response response2 = order.makeOrder(ingredients, bearerToken);

        response2.then()
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCrateOrderUnauthorized(){
        Response response1 = user.createUser(email, password, name);
        //Response response1 = user.loginUser(email, password);
        bearerToken = response1.jsonPath().getString("accessToken");


        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d"};

        Response response = order.makeOrder(ingredients, bearerToken);

        response.then().statusCode(401).body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void testCreateOrderWithIngredients(){
        user.createUser(email, password, name);
        Response loginResponse = user.loginUser(email, password);
        bearerToken = loginResponse.jsonPath().getString("accessToken");

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72"};

        Response response = order.makeOrder(ingredients, bearerToken);

        response.then()
                .statusCode(200)
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("success", equalTo(true));
    }


    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients(){
        user.createUser(email, password, name);
        Response loginResponse = user.loginUser(email, password);
        bearerToken = loginResponse.jsonPath().getString("accessToken");

        String[] ingredients = {};

        Response response = order.makeOrder(ingredients, bearerToken);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void testCreateOrderWithInvalidIngredientHash(){
        user.createUser(email, password, name);
        Response loginResponse = user.loginUser(email, password);
        bearerToken = loginResponse.jsonPath().getString("accessToken");

        String[] ingredients = {"invalidhash"};

        Response response = order.makeOrder(ingredients, bearerToken);

        response.then()
                .statusCode(500);
    }



}
