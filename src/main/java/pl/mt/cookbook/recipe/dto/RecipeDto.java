package pl.mt.cookbook.recipe.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 255)
    private String title;

    @NotBlank
    @Size(min = 2, max = 500)
    private String description;

    @Positive
    private int portion;

    @NotBlank
    @Pattern(regexp = "^([^;-]+-[^;-]+;)+$",
            message = "Składniki rozdzielone średnikami (;), nazwa i ilość rozdzielone myślnikiem (-)")
    private String ingredients;

    @NotBlank
    @Size(min = 2, max = 8000)
    private String preparation;

    @NotBlank
    @Size(min = 2, max = 2000)
    private String hints;

    private String img;

    private LocalDateTime dateAdded;

    private int likes;

    private List<String> likedByEmail;

    @NotEmpty(message = "Należy wybrać co najmniej jedną kategorię")
    private List<Long> categoryIds;

    private boolean nonPublic;
}