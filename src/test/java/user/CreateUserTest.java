package user;

import dto.UserRequest;
import generator.UserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private UserSteps userSteps;
    private String accessToken;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @After
    @DisplayName("Удаление пользователя после каждого теста при получении токена")
    public  void tearDown(){
        if (accessToken != null){
            userSteps.deleteUser(accessToken)
                    .assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Регистрация нового пользователя с валидными значениями")
    public void checkCreateUserWithValidCredentials(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Регистрация нового пользователя без поля email")
    public void checkCreateUserWithoutEmailField(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequestWithoutEmailField();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Регистрация нового пользователя без поля name")
    public void checkCreateUserWithoutNameField(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequestWithoutNameField();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Регистрация нового пользователя без поля password")
    public void checkCreateUserWithoutPasswordField(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequestWithoutPasswordField();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Регистрация нового пользователя, который уже зарегистрирован")
    public void checkCreateUserWhoIsAlreadyCreate(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
    }


}
