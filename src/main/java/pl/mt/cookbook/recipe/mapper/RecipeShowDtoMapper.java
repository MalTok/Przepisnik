package pl.mt.cookbook.recipe.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.recipe.IngredientAmount;
import pl.mt.cookbook.recipe.Recipe;
import pl.mt.cookbook.recipe.dto.RecipeShowDto;

import java.util.List;

@Service
public class RecipeShowDtoMapper {

    public RecipeShowDto maptoShowDto(Recipe recipe) {
        RecipeShowDto recipeShowDto = new RecipeShowDto();
        recipeShowDto.setId(recipe.getId());
        recipeShowDto.setTitle(recipe.getTitle());
        recipeShowDto.setDescription(recipe.getDescription());
        recipeShowDto.setPortion(recipe.getPortion());
        List<Long> ingredients = recipe.getIngredients().stream()
                .map(IngredientAmount::getId)
                .toList();
        recipeShowDto.setIngredientIds(ingredients);
        recipeShowDto.setPreparation(recipe.getPreparation());
        recipeShowDto.setHints(recipe.getHints());
        recipeShowDto.setImg(recipe.getImg());
        recipeShowDto.setDateAdded(recipe.getDateAdded());
        recipeShowDto.setLikes(recipe.getLikes());
        recipeShowDto.setAddedByNickname(recipe.getAddedByNickname());
        return recipeShowDto;
    }
}
