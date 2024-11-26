import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    String CREATE_ORDER_URL = "/api/orders";
    String GET_ORDER_LIST = "/api/orders/all";
    String GET_USER_ORDER_INFO = "/api/orders";
    String GET_INGREDIENTS = "/api/ingredients";

    @Step("Получить данные об ингредиентах")
    public Response getIngredients(){
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(GET_INGREDIENTS);

    }

    @Step("Создать заказ")
    public Response makeOrder(String[] ingredients, String bearerToken){
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", ingredients);

        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .log().all()
                .body(requestBody)
                .when()
                .post(CREATE_ORDER_URL);
    }

    @Step("Получить все заказы")
    public Response getAllOrders(){
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get(GET_ORDER_LIST);

        response.then().statusCode(200);

        return response;
    }

    @Step("Получить заказы конкретного пользователя")
    public Response getUserOrder(String bearerToken){
        return
                given()
                        .headers("Content-type", "application/json", "Authorization", bearerToken)
                        .log().all()
                        .when()
                        .get(GET_USER_ORDER_INFO);
    }






}
