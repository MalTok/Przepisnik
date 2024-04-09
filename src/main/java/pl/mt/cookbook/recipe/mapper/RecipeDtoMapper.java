package pl.mt.cookbook.recipe.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.category.CategoryRepository;
import pl.mt.cookbook.ingredient.Ingredient;
import pl.mt.cookbook.ingredient.IngredientRepository;
import pl.mt.cookbook.recipe.IngredientAmount;
import pl.mt.cookbook.recipe.Recipe;
import pl.mt.cookbook.recipe.dto.RecipeDto;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeDtoMapper {
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public RecipeDtoMapper(CategoryRepository categoryRepository,
                           IngredientRepository ingredientRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
    }

    public Recipe map(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPortion(recipeDto.getPortion());
        List<Category> categoryList = new ArrayList<>();
        for (Long id : recipeDto.getCategoryIds()) {
            Optional<Category> byId = categoryRepository.findById(id);
            byId.ifPresent(categoryList::add);
        }
        recipe.setCategories(categoryList);
        getIngredients(recipeDto, recipe).forEach(recipe::addIngredientAmount);
        recipe.setPreparation(recipeDto.getPreparation());
        recipe.setHints(recipeDto.getHints());
        recipe.setImg(recipeDto.getImg());
        recipe.setDateAdded(LocalDateTime.now());
        User user = getUser();
        recipe.setAddedByNickname(user.getNickname());
        recipe.setAddedByEmail(user.getEmail());
        recipe.setUsers(new ArrayList<>());
        recipe.setNonPublic(recipeDto.isNonPublic());
        return recipe;
    }

    private User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }

    public List<IngredientAmount> getIngredients(RecipeDto recipeDto, Recipe recipe) {
        List<IngredientAmount> list = new ArrayList<>();
        String ingredients = recipeDto.getIngredients();
        String[] ingredientsArray = ingredients.split(";");
        for (String element : ingredientsArray) {
            String[] split = element.split("-");
            Ingredient ingredient = getIngredient(split);
            IngredientAmount ingredientAmount = new IngredientAmount(recipe, ingredient, split[1]);
            ingredient.addIngredientAmount(ingredientAmount);
            list.add(ingredientAmount);
            ingredientRepository.save(ingredient);
        }
        return list;
    }

    private Ingredient getIngredient(String[] split) {
        String name = split[0];
        Ingredient ingredient;
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByNameIgnoreCase(name);
        if (optionalIngredient.isPresent()) {
            ingredient = optionalIngredient.get();
        } else {
            ingredient = new Ingredient();
            ingredient.setName(split[0]);
        }
        return ingredient;
    }

    public RecipeDto mapRecipeToDto(Recipe recipe) {
        String ingredientsOutputFormat = getIngredientsOutputFormat(recipe);
        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getPortion(),
                ingredientsOutputFormat,
                recipe.getPreparation(),
                recipe.getHints(),
                recipe.getImg(),
                recipe.getDateAdded(),
                recipe.getUsers().size(),
                getLikedByUsersList(recipe),
                getCategoryIdList(recipe),
                recipe.isNonPublic()
        );
    }

    private List<Long> getCategoryIdList(Recipe recipe) {
        List<Long> categoryDtoList = new ArrayList<>();
        for (Category category : recipe.getCategories()) {
            categoryDtoList.add(category.getId());
        }
        return categoryDtoList;
    }

    private String getIngredientsOutputFormat(Recipe recipe) {
        StringBuilder stringBuilder = new StringBuilder();
        for (IngredientAmount ingredientAmount : recipe.getIngredients()) {
            stringBuilder.append(ingredientAmount.getIngredient().getName())
                    .append("-")
                    .append(ingredientAmount.getAmount())
                    .append(";");
        }
        return stringBuilder.toString();
    }

    public List<RecipeDto> maptoDtos(List<Recipe> recipeList) {
        return recipeList.stream()
                .map(this::mapRecipeToDto)
                .toList();
    }

    private List<String> getLikedByUsersList(Recipe recipe) {
        return recipe.getUsers()
                .stream()
                .map(User::getEmail)
                .toList();
    }
}
