package pl.mt.cookbook.recipe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeShowDto {
    private Long id;

    private String title;

    private String description;

    private int portion;

    private List<Long> ingredientIds;

    private String preparation;

    private String hints;

    private String img;

    private LocalDateTime dateAdded;

    private String addedByNickname;

    private int likes;

    private List<String> likedByEmail;
}