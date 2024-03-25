package pl.mt.cookbook.recipe;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.mt.cookbook.recipe.dto.RecipeDto;
import pl.mt.cookbook.recipe.dto.RecipeShowDto;

import java.util.List;
import java.util.Optional;

@RequestMapping("/recipe")
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientAmountRepository ingredientAmountRepository;

    public RecipeController(
            RecipeService recipeService,
            IngredientAmountRepository ingredientAmountRepository) {
        this.recipeService = recipeService;
        this.ingredientAmountRepository = ingredientAmountRepository;
    }

    @GetMapping("/{id}")
    public String showRecipe(@PathVariable Long id, Model model) {
        Optional<RecipeShowDto> foundRecipe = recipeService.findAndReturnShowDto(id);
        if (foundRecipe.isPresent()) {
            RecipeShowDto recipeShowDto = foundRecipe.get();
            List<Long> ingredientIds = recipeShowDto.getIngredientIds();
            List<IngredientAmount> ingredientAmountList = ingredientAmountRepository.findAllByIdIn(ingredientIds);
            model.addAttribute("ingredients", ingredientAmountList);
            model.addAttribute("recipe", recipeShowDto);
        }
        return "recipe";
    }

    @GetMapping("/add")
    public String addRecipe(Model model) {
        model.addAttribute("recipe", new RecipeDto());
        return "recipe-form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("recipe") RecipeDto recipeDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipeDto);
            return "recipe-form";
        } else {
            Long id = recipeService.save(recipeDto);
            return "redirect:/recipe/" + id;
        }
    }

    @GetMapping("/edit/{id}")
    public String editRecipe(@PathVariable Long id, Model model) {
        Optional<RecipeDto> foundRecipe = recipeService.findAndReturnDto(id);
        if (foundRecipe.isPresent()) {
            RecipeDto recipeDto = foundRecipe.get();
            model.addAttribute("recipe", recipeDto);
        }
        return "recipe-form-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("recipe") RecipeDto recipeDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipeDto);
            return "recipe-form-edit";
        } else {
            recipeService.update(id, recipeDto);
            return "redirect:/recipe/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        recipeService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/like/{id}")
    public String like(@PathVariable Long id) {
        recipeService.like(id);
        return "redirect:/recipe/" + id;
    }
}