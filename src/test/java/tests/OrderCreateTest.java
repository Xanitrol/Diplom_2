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

import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void createOrderWithAuthSuccess() {
        User user = createRandomUser();
        registerUserAndSaveToken(user);

        OrderRequest request = new OrderRequest(getValidIngredientIds());

        Response response = orderClient.createOrderWithAuth(request, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка успешного создания заказа без авторизации")
    public void createOrderWithoutAuthSuccess() {
        OrderRequest request = new OrderRequest(getValidIngredientIds());

        Response response = orderClient.createOrderWithoutAuth(request);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать заказ без ингредиентов")
    @Description("Проверка ошибки при создании заказа с пустым списком ингредиентов")
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
    @Description("Проверка ошибки при создании заказа с неверным ingredient hash")
    public void createOrderWithInvalidIngredient() {
        OrderRequest request = new OrderRequest(Collections.singletonList("invalid_hash"));

        Response response = orderClient.createOrderWithoutAuth(request);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false));
    }
}