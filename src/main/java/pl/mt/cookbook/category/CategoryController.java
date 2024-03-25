package pl.mt.cookbook.category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mt.cookbook.category.dto.CategoryAddDto;
import pl.mt.cookbook.category.dto.CategoryPreviewDto;
import pl.mt.cookbook.category.dto.CategoryRemoveDto;
import pl.mt.cookbook.recipe.RecipeService;
import pl.mt.cookbook.recipe.dto.RecipeDto;

import java.util.List;

@RequestMapping("/category")
@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final RecipeService recipeService;

    public CategoryController(CategoryService categoryService, RecipeService recipeService) {
        this.categoryService = categoryService;
        this.recipeService = recipeService;
    }

    @GetMapping("/{url}")
    public String category(@PathVariable String url, Model model) {
        CategoryPreviewDto categoryPreviewDto = categoryService.findByUrl(url);
        List<Long> recipeIds = categoryPreviewDto.getRecipeIds();
        List<RecipeDto> recipeList = recipeService.findAllByIdIn(recipeIds);
        if (recipeList.isEmpty()) {
            model.addAttribute("categoryMessage", true);
        }
        model.addAttribute("filteredCategory", categoryPreviewDto);
        model.addAttribute("recipeList", recipeList);

        return "category-recipes";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new CategoryAddDto());
        return "category-form";
    }

    @PostMapping("/add")
    public String add(CategoryAddDto categoryAddDto) {
        CategoryAddDto returnedCategory = categoryService.save(categoryAddDto);
        String url = returnedCategory.getUrl();
        return "redirect:/category/" + url;
    }

    @GetMapping("/remove")
    public String removeForm(Model model) {
        model.addAttribute("categoryToRemove", new CategoryRemoveDto());
        return "category-remove";
    }

    @PostMapping("/remove")
    public String remove(CategoryRemoveDto categoryRemoveDto) {
        Long id = categoryRemoveDto.getId();
        categoryService.remove(id);
        return "redirect:/";
    }
}
