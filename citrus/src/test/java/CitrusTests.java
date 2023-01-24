import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.JsonSupport.json;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.TestNGCitrusSupport;
import dto.Support;
import dto.UserDTO;
import dto.UserData;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class CitrusTests extends TestNGCitrusSupport {

  private TestContext context;

  /**
   * Проверка успешного получения пользователя
   * Действие:
   * - Для получения пользователя отправляем get запрос на url users/${userId}
   * Ожидаемый результат:
   * - статус ответа равен 200
   * - ответ совпадает с ожидаемым
   * - схема ответа соответсвует jsonSchema 'userSchema.json'
   */

  @Test(description = "Получение информации о пользователе")
  @CitrusTest
  public void getTestActions() {
    this.context = citrus.getCitrusContext().createTestContext();

    $(http()
        .client("restClient")
        .send()
        .get("users/${userId}")
    );

    $(http()
        .client("restClient")
        .receive()
        .response(HttpStatus.OK)
        .message()
        .body(new ObjectMappingPayloadBuilder(getUserJson(), "objectMapper"))
        .validate(json()
            .schemaValidation(true)
            .schema("userSchema"))
    );
  }

  /**
   * Проверка что пользователь не найден
   * Действие:
   * - ищем пользователя с Id = 3
   * Ожидаемый результат:
   * - статус ответа равен 404
   * - error = "User with id = 3 not found"
   */

  @Test(description = "Пользователь не найден")
  @CitrusTest
  public void getUserNotFound() {
    this.context = citrus.getCitrusContext().createTestContext();

    $(http()
        .client("restClient")
        .send()
        .get("users/3")
    );

    $(http()
        .client("restClient")
        .receive()
        .response(HttpStatus.NOT_FOUND)
        .message()
        .type("application/json")
        .validate(jsonPath().expression("$.error", "User with id = 3 not found"))
    );
  }

  /**
   * Некорректный запрос получения списка пользователя
   * Действие:
   * - открываем url без параметра page
   * Ожидаемый результат:
   * - статус ответа равен 400
   * - error = "Bad Request, parameter page is required"
   */
  @Test(description = "Некорректный запрос получения списка пользователя")
  @CitrusTest
  public void getUserBadRequest() {
    this.context = citrus.getCitrusContext().createTestContext();

    $(http()
        .client("restClient")
        .send()
        .get("users")
    );

    $(http()
        .client("restClient")
        .receive()
        .response(HttpStatus.BAD_REQUEST)
        .message()
        .type("application/json")
        .validate(jsonPath().expression("$.error", "Bad Request, parameter page is required"))
    );
  }

  @Test(description = "Проверяем что в списке пользователей 6 пользователей")
  @CitrusTest
  public void getUserList() {
    this.context = citrus.getCitrusContext().createTestContext();

    $(http()
        .client("restClient")
        .send()
        .get("users?page=1")
    );

    $(http()
        .client("restClient")
        .receive()
        .response(HttpStatus.OK)
        .message()
        .type("application/json")
        .validate(jsonPath().expression("$.data.length()", 6))
        .validate(json()
            .schemaValidation(true)
            .schema("userListSchema"))
    );
  }

  public UserDTO getUserJson() {
    UserDTO user = UserDTO
        .builder()
        .data(UserData.builder()
            .id(Integer.parseInt(context.getVariable("userId")))
            .email("janet.weaver@reqres.in")
            .firstName("Janet")
            .lastName("Weaver")
            .avatar("https://reqres.in/img/faces/2-image.jpg")
            .build())
        .support(Support.builder()
            .url("https://reqres.in/#support-heading")
            .text("To keep ReqRes free, contributions towards server costs are appreciated!")
            .build())
        .build();

    return user;
  }
}
