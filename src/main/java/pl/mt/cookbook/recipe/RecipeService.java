package pl.mt.cookbook.recipe;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.category.CategoryRepository;
import pl.mt.cookbook.recipe.dto.RecipeDto;
import pl.mt.cookbook.recipe.dto.RecipeShowDto;
import pl.mt.cookbook.recipe.mapper.RecipeDtoMapper;
import pl.mt.cookbook.recipe.mapper.RecipeShowDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeDtoMapper recipeDtoMapper;
    private final RecipeShowDtoMapper recipeShowDtoMapper;

    public RecipeService(
            RecipeRepository recipeRepository,
            RecipeDtoMapper recipeDtoMapper,
            CategoryRepository categoryRepository,
            RecipeShowDtoMapper recipeShowDtoMapper
    ) {
        this.recipeRepository = recipeRepository;
        this.recipeDtoMapper = recipeDtoMapper;
        this.categoryRepository = categoryRepository;
        this.recipeShowDtoMapper = recipeShowDtoMapper;
    }

    public Optional<Recipe> find(Long id) {
        return recipeRepository.findById(id);
    }

    public Optional<RecipeShowDto> findAndReturnShowDto(Long id) {
        return find(id).map(recipeShowDtoMapper::maptoShowDto);
    }

    public Optional<RecipeDto> findAndReturnDto(Long id) {
        return find(id).map(recipeDtoMapper::mapRecipeToDto);
    }

    public List<RecipeDto> findByTitleContainingWord(String word) {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByTitleContainingIgnoreCase(word));
    }

    public List<RecipeDto> findAll() {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAll());
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional
    public Long save(RecipeDto recipeDto) {
        Recipe recipe = recipeDtoMapper.map(recipeDto);
        recipeRepository.save(recipe);
        return recipe.getId();
    }

    @Transactional
    public void like(Long id) {
        Optional<Recipe> optionalRecipe = find(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            int likes = recipe.getLikes() + 1;
            recipe.setLikes(likes);
        }
    }

    public List<RecipeDto> findAllSortedByLikes() {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllSortedByLikes());
    }

    @Transactional
    public void update(Long id, RecipeDto recipeDto) {
        Optional<Recipe> optionalRecipe = find(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setTitle(recipeDto.getTitle());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setPortion(recipeDto.getPortion());
            List<Category> categoryList = new ArrayList<>();
            for (Long ids : recipeDto.getCategoryIds()) {
                Optional<Category> byId = categoryRepository.findById(ids);
                byId.ifPresent(categoryList::add);
            }
            recipe.getIngredients().clear();
            recipeDtoMapper.getIngredients(recipeDto, recipe).forEach(recipe::addIngredientAmount);
            recipe.setCategories(categoryList);
            recipe.setPreparation(recipeDto.getPreparation());
            recipe.setHints(recipeDto.getHints());
            recipe.setImg(recipeDto.getImg());
        }
    }

    public List<RecipeDto> findAllByIdIn(List<Long> ids) {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByIdIn(ids));
    }

    public List<RecipeDto> findAllAddedByUser(String nickname) {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByAddedByNickname(nickname));
    }
}