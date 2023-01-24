package services;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
  public static final String BASE_URI = "http://localhost:7777/api/";

  public final RequestSpecification specification;

  public BaseApi() {
    this.specification = given()
        .baseUri(BASE_URI)
        .contentType(ContentType.JSON)
        .log().all();
  }
}
