package orderTest;

import dto.Order;
import dto.UserRequest;
import generator.IngredientGenerator;
import generator.UserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;
    private UserRequest userRequest;
    private  Order order;

    private  List<String> ingredientsList;
    @Before
    public void setUp(){
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userRequest = UserRequestGenerator.getRandomUserRequest();
        accessToken = userSteps.create(userRequest).extract().path("accessToken");
        ingredientsList = orderSteps.getAllIngredients().extract().path("data._id");
    }
    @After
    @DisplayName("Удаление пользователя после каждого теста при получении токена")
    //удаляем данные после каждого теста
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken).assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    public void checkCreateOrderWithAuthorization() {
        order = new Order(IngredientGenerator.getRandomIngredients(ingredientsList));
        orderSteps.createOrderWithAuthorization(order, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void checkCreateOrderWithoutIngredientsAndWithoutAuthorization() {
        order = new Order(IngredientGenerator.getRandomIngredients(null));
        orderSteps.createOrderWithoutAuthorization(order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но без ингредиентов")
    public void checkCreateOrderWithoutIngredients() {
        order = new Order(IngredientGenerator.getRandomIngredients(null));;
        orderSteps.createOrderWithAuthorization(order, accessToken)
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }

    @Test
    @DisplayName("Создание заказа без авторизации, но с ингредиентами")
    public void checkCreateOrderWithoutAuthorization() {
        order = new Order(IngredientGenerator.getRandomIngredients(ingredientsList));
        orderSteps.createOrderWithoutAuthorization(order)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией,но с неверным хэшэм ингредиентов")
    public void checkCreateOrderWithInvalidIngredients() {
        List<String> newEngredients = List.of("93jf6godh83jbitr894krt", "53jf6oent83jbitr032krt", "53jf6oent83jbitr032kji");
        order = new Order(IngredientGenerator.getRandomIngredients(newEngredients));
        orderSteps.createOrderWithAuthorization(order, accessToken)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
