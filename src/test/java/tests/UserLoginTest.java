package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.LoginRequest;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseTest {

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка успешного логина ранее созданного пользователя")
    public void loginUserSuccess() {
        User user = createRandomUser();
        registerUserAndSaveToken(user);

        Response loginResponse = loginUser(user);

        loginResponse.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Нельзя залогиниться с неверными данными")
    @Description("Проверка ошибки авторизации при неверном email и password")
    public void loginWithInvalidCredentialsUnauthorized() {
        LoginRequest loginRequest = new LoginRequest(
                "wrong" + System.currentTimeMillis() + "@yandex.ru",
                "wrongPassword"
        );

        Response response = userClient.loginUser(loginRequest);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}