package base;

import client.IngredientClient;
import client.UserClient;
import data.UserGenerator;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.LoginRequest;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class BaseTest {

    protected final UserClient userClient = new UserClient();
    protected final IngredientClient ingredientClient = new IngredientClient();
    protected String accessToken;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            try {
                userClient.deleteUser(accessToken);
            } catch (Exception ignored) {
            }
        }
    }

    @Step("Создать случайного пользователя")
    protected User createRandomUser() {
        return UserGenerator.getRandomUser();
    }

    @Step("Зарегистрировать пользователя")
    protected Response registerUser(User user) {
        return userClient.createUser(user);
    }

    @Step("Создать пользователя и сохранить токен")
    protected Response registerUserAndSaveToken(User user) {
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        return response;
    }

    @Step("Логин пользователя")
    protected Response loginUser(User user) {
        return userClient.loginUser(new LoginRequest(user.getEmail(), user.getPassword()));
    }

    @Step("Получить два валидных id ингредиентов")
    protected List<String> getValidIngredientIds() {
        Response response = ingredientClient.getIngredients();

        response.then().statusCode(200);

        String firstIngredientId = response.then().extract().path("data[0]._id");
        String secondIngredientId = response.then().extract().path("data[1]._id");

        return List.of(firstIngredientId, secondIngredientId);
    }

    @Step("Получить один валидный id ингредиента")
    protected String getValidIngredientId() {
        Response response = ingredientClient.getIngredients();

        response.then().statusCode(200);

        return response.then().extract().path("data[0]._id");
    }
}