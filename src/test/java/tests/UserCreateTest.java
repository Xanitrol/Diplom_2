package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void createUserSuccess() {
        User user = createRandomUser();

        Response response = registerUserAndSaveToken(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать уже существующего пользователя")
    @Description("Проверка ошибки при повторной регистрации одного и того же пользователя")
    public void createDuplicateUserForbidden() {
        User user = createRandomUser();

        registerUserAndSaveToken(user);
        Response secondResponse = registerUser(user);

        secondResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без обязательного поля")
    @Description("Проверка ошибки при создании пользователя без email")
    public void createUserWithoutRequiredFieldForbidden() {
        User user = new User(null, "password123", "testName");

        Response response = registerUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}