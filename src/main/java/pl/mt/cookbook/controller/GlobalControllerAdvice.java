package pl.mt.cookbook.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.mt.cookbook.category.CategoryController;
import pl.mt.cookbook.category.CategoryService;
import pl.mt.cookbook.category.dto.CategoryMenuDto;
import pl.mt.cookbook.recipe.RecipeController;

import java.util.List;

@ControllerAdvice(assignableTypes = {CategoryController.class, RecipeController.class, HomeController.class})
class GlobalControllerAdvice {
    private final CategoryService categoryService;

    public GlobalControllerAdvice(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<CategoryMenuDto> getAvailableCategories() {
        return categoryService.findAll();
    }
}
