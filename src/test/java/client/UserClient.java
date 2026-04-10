package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.LoginRequest;
import model.UpdateUserRequest;
import model.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_PATH = "/api/auth";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(BASE_PATH + "/register");
    }

    @Step("Логин пользователя")
    public Response loginUser(LoginRequest loginRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .post(BASE_PATH + "/login");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .delete(BASE_PATH + "/user");
    }

    @Step("Обновление пользователя с авторизацией")
    public Response updateUserWithAuth(UpdateUserRequest request, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(request)
                .patch(BASE_PATH + "/user");
    }

    @Step("Обновление пользователя без авторизации")
    public Response updateUserWithoutAuth(UpdateUserRequest request) {
        return given()
                .header("Content-type", "application/json")
                .body(request)
                .patch(BASE_PATH + "/user");
    }
}