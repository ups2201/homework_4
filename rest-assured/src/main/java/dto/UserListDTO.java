package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class UserListDTO {

  private int page;
  @JsonProperty("per_page")
  private int perPage;
  private int total;
  @JsonProperty("total_pages")
  private int totalPages;
  private List<UserData> data;
  private Support support;
}