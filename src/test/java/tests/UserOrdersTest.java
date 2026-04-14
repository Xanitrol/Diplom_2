package tests;

import base.BaseTest;
import client.OrderClient;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserOrdersTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка успешного получения заказов конкретного пользователя")
    public void getOrdersAuthorizedUserSuccess() {
        User user = createRandomUser();
        registerUserAndSaveToken(user);
        String token = loginUser(user).then().extract().path("accessToken");

        Response response = orderClient.getUserOrders(token);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Нельзя получить заказы пользователя без авторизации")
    @Description("Проверка ошибки при получении заказов без accessToken")
    public void getOrdersWithoutAuthorizationUnauthorized() {
        Response response = orderClient.getUserOrdersWithoutAuth();

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}