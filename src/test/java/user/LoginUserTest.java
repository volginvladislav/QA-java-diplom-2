package user;

import dto.LoginRequest;
import dto.UserRequest;
import generator.LoginRequestGenerator;
import generator.UserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
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
    @DisplayName("Логин под существующим пользователем")
    public void checkLoginCreatedUser(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        LoginRequest loginRequest = LoginRequestGenerator.from(userRequest);
        userSteps.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин c неверным email")
    public void checkLoginCreatedUserWithInvalidEmailField(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        LoginRequest loginRequest = LoginRequestGenerator.getLoginRequestWithInvalidEmailField(userRequest);
        userSteps.login(loginRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Логин c неверным password")
    public void checkLoginCreatedUserWithInvalidPasswordField(){
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        LoginRequest loginRequest = LoginRequestGenerator.getLoginRequestWithInvalidPasswordField(userRequest);
        userSteps.login(loginRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }
}
