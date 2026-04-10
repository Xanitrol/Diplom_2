package tests;

import base.BaseTest;
import client.OrderClient;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.OrderRequest;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void createOrderWithAuthSuccess() {
        User user = createRandomUser();
        registerUserAndSaveToken(user);

        OrderRequest request = new OrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f"
        ));

        Response response = orderClient.createOrderWithAuth(request, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthSuccess() {
        OrderRequest request = new OrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f"
        ));

        Response response = orderClient.createOrderWithoutAuth(request);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать заказ без ингредиентов")
    public void createOrderWithoutIngredients() {
        OrderRequest request = new OrderRequest(Collections.emptyList());

        Response response = orderClient.createOrderWithoutAuth(request);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Нельзя создать заказ с невалидным ингредиентом")
    public void createOrderWithInvalidIngredient() {
        OrderRequest request = new OrderRequest(List.of("invalid_hash"));

        Response response = orderClient.createOrderWithoutAuth(request);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false));
    }
}