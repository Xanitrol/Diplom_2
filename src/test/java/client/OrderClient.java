package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(OrderRequest request, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(request)
                .post(ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(OrderRequest request) {
        return given()
                .header("Content-type", "application/json")
                .body(request)
                .post(ORDERS);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .get(ORDERS);
    }

    @Step("Получение заказов без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .get(ORDERS);
    }
}