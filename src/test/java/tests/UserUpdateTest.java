package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.UpdateUserRequest;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest extends BaseTest {

    @Test
    @DisplayName("Изменение email и name авторизованного пользователя")
    @Description("Проверка успешного изменения данных пользователя с авторизацией")
    public void updateUserWithAuthorizationSuccess() {
        User user = createRandomUser();
        registerUserAndSaveToken(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "updated" + System.currentTimeMillis() + "@yandex.ru",
                "updatedName",
                user.getPassword()
        );

        Response response = userClient.updateUserWithAuth(updateUserRequest, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updateUserRequest.getEmail().toLowerCase()))
                .body("user.name", equalTo(updateUserRequest.getName()));
    }

    @Test
    @DisplayName("Изменение только имени авторизованного пользователя")
    @Description("Проверка, что можно изменить одно поле пользователя")
    public void updateOnlyNameWithAuthorizationSuccess() {
        User user = createRandomUser();
        registerUserAndSaveToken(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                user.getEmail(),
                "anotherUpdatedName",
                user.getPassword()
        );

        Response response = userClient.updateUserWithAuth(updateUserRequest, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updateUserRequest.getEmail().toLowerCase()))
                .body("user.name", equalTo(updateUserRequest.getName()));
    }

    @Test
    @DisplayName("Нельзя изменить данные пользователя без авторизации")
    @Description("Проверка ошибки изменения данных без accessToken")
    public void updateUserWithoutAuthorizationUnauthorized() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "updated" + System.currentTimeMillis() + "@yandex.ru",
                "updatedName",
                "password123"
        );

        Response response = userClient.updateUserWithoutAuth(updateUserRequest);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}