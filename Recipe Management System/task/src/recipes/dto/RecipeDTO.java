package recipes.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Size(min = 1)
    private List<String> ingredients;
    @NotNull
    @Size(min = 1)
    private List<String> directions;
}
