
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    String CREATE_ORDER_URL = "/api/orders";
    String GET_ORDER_LIST = "/api/orders/all";
    String GET_USER_ORDER_INFO = "/api/orders";

    @Step("Создать заказ")
    public Response makeOrder(String[] ingredients){
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", ingredients);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .get(CREATE_ORDER_URL);

        response.then().statusCode(201);

        return response;
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
        Response response =
                given()
                        .headers("Content-type", "application/json", "Authorization", "Bearer " + bearerToken)
                        .log().all()
                        .when()
                        .get(GET_USER_ORDER_INFO);

        response.then().statusCode(200);

        return response;

    }




}
