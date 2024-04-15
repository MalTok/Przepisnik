package pl.mt.cookbook.category.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.category.dto.CategoryPreviewDto;
import pl.mt.cookbook.recipe.Recipe;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryPreviewDtoMapper {
    public CategoryPreviewDto mapToPreviewDto(Category category) {
        CategoryPreviewDto categoryPreviewDto = new CategoryPreviewDto();
        categoryPreviewDto.setName(category.getName());
        categoryPreviewDto.setDescription(category.getDescription());
        categoryPreviewDto.setImg(category.getImg());
        List<Long> recipeIds = getRecipeIds(category);
        categoryPreviewDto.setRecipeIds(recipeIds);
        return categoryPreviewDto;
    }

    private static List<Long> getRecipeIds(Category category) {
        return category.getRecipes()
                .stream()
                .map(Recipe::getId)
                .collect(Collectors.toList());
    }
}
