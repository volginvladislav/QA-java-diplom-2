package generator;

import dto.UserRequest;
import dto.LoginRequest;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class LoginRequestGenerator {
    @Step("Ввод данных для проверки логина в системе")
    public static LoginRequest from(UserRequest userRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(userRequest.getPassword());
        return loginRequest;
    }
    @Step ("Данные для проверки входа с неверным email")
    public static LoginRequest getLoginRequestWithInvalidEmailField(UserRequest userRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        loginRequest.setPassword(userRequest.getPassword());
        return loginRequest;
    }
    @Step("Данные для проверки входа с неверным password")
    public static LoginRequest getLoginRequestWithInvalidPasswordField(UserRequest userRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(RandomStringUtils.randomAlphanumeric(8));
        return loginRequest;
    }
}
