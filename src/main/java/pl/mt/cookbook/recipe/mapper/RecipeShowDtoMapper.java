package pl.mt.cookbook.recipe.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.recipe.IngredientAmount;
import pl.mt.cookbook.recipe.Recipe;
import pl.mt.cookbook.recipe.dto.RecipeShowDto;
import pl.mt.cookbook.user.User;

import java.util.List;

@Service
public class RecipeShowDtoMapper {

    public RecipeShowDto maptoShowDto(Recipe recipe) {
        RecipeShowDto recipeShowDto = new RecipeShowDto();
        recipeShowDto.setId(recipe.getId());
        recipeShowDto.setTitle(recipe.getTitle());
        recipeShowDto.setDescription(recipe.getDescription());
        recipeShowDto.setPortion(recipe.getPortion());
        List<Long> ingredients = getIngredients(recipe);
        recipeShowDto.setIngredientIds(ingredients);
        recipeShowDto.setPreparation(recipe.getPreparation());
        recipeShowDto.setHints(recipe.getHints());
        recipeShowDto.setImg(recipe.getImg());
        recipeShowDto.setDateAdded(recipe.getDateAdded());
        recipeShowDto.setAddedByNickname(recipe.getAddedByNickname());
        recipeShowDto.setAddedByEmail(recipe.getAddedByEmail());
        recipeShowDto.setLikes(getLikesListSize(recipe));
        recipeShowDto.setLikedByEmail(getLikedByUsersList(recipe));
        recipeShowDto.setNonPublic(recipe.isNonPublic());
        return recipeShowDto;
    }

    private List<Long> getIngredients(Recipe recipe) {
        return recipe.getIngredients().stream()
                .map(IngredientAmount::getId)
                .toList();
    }

    private int getLikesListSize(Recipe recipe) {
        return recipe.getUsers().size();
    }

    private List<String> getLikedByUsersList(Recipe recipe) {
        return recipe.getUsers()
                .stream()
                .map(User::getEmail)
                .toList();
    }
}
