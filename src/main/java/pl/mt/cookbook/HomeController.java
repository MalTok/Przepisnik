package pl.mt.cookbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.mt.cookbook.recipe.RecipeService;
import pl.mt.cookbook.recipe.dto.RecipeDto;

import java.util.List;

@Controller
class HomeController {
    private final RecipeService recipeService;

    public HomeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<RecipeDto> recipeList = recipeService.findAll();
        if (recipeList.isEmpty()) {
            model.addAttribute("emptyMessage", true);
        }
        model.addAttribute("recipeList", recipeList);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String word,
                         Model model) {
        List<RecipeDto> recipeList = recipeService.findByTitleContainingWord(word);
        if (recipeList.isEmpty()) {
            model.addAttribute("searchMessage", true);
            model.addAttribute("searchedWord", word);
        }
        model.addAttribute("recipeList", recipeList);
        return "search-result";
    }

    @GetMapping("/best")
    public String best(Model model) {
        List<RecipeDto> recipeList = recipeService.findAllSortedByLikes();
        if (recipeList.isEmpty()) {
            model.addAttribute("emptyMessage", true);
        }
        model.addAttribute("recipeList", recipeList);
        return "best-recipes";
    }
}