package pl.mt.cookbook.recipe;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.recipe.dto.RecipeDto;
import pl.mt.cookbook.recipe.dto.RecipeShowDto;
import pl.mt.cookbook.recipe.mapper.RecipeDtoMapper;
import pl.mt.cookbook.recipe.mapper.RecipeShowDtoMapper;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeDtoMapper recipeDtoMapper;
    private final RecipeShowDtoMapper recipeShowDtoMapper;
    private final UserRepository userRepository;

    public RecipeService(
            RecipeRepository recipeRepository,
            RecipeDtoMapper recipeDtoMapper,
            RecipeShowDtoMapper recipeShowDtoMapper,
            UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeDtoMapper = recipeDtoMapper;
        this.recipeShowDtoMapper = recipeShowDtoMapper;
        this.userRepository = userRepository;
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

    public List<RecipeDto> findByTitleContainingWordIgnoringCase(String word) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return recipeDtoMapper.maptoDtos(recipeRepository.findAllByTitleContainingIgnoreCaseAndNonPublicIsFalse(word));
        } else {
            String email = getEmail();
            return recipeDtoMapper.maptoDtos(recipeRepository.findAllByTitleIgnoreCaseIsWhenPublicOrPrivateAndAddedByEmailIs(word, email));
        }
    }

    public List<RecipeDto> findAllPublic() {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByNonPublicIsFalse());
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
        optionalRecipe.ifPresent(this::like);
    }

    private void like(Recipe recipe) {
        List<User> users = recipe.getUsers();
        String email = getEmail();
        addUserToLikesList(email, users);
    }

    private void addUserToLikesList(String email, List<User> users) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        userOptional.ifPresentOrElse(
                user -> {
                    if (!users.contains(user)) {
                        users.add(user);
                    }
                },
                () -> {
                    throw new EntityNotFoundException("User not found.");
                }
        );
    }

    private String getEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Transactional
    public void unlike(Long id) {
        Optional<Recipe> optionalRecipe = find(id);
        optionalRecipe.ifPresent(this::unlike);
    }

    private void unlike(Recipe recipe) {
        List<User> users = recipe.getUsers();
        String email = getEmail();
        removeUserFromLikesList(email, users);
    }

    private void removeUserFromLikesList(String email, List<User> users) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        userOptional
                .map(users::remove)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<RecipeDto> findAllPublicSortedByLikes() {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllPublicSortedByLikes());
    }

    @Transactional
    public void update(Long id, RecipeDto recipeDto) {
        Optional<Recipe> optionalRecipe = find(id);
        optionalRecipe.ifPresent(recipe -> update(recipeDto, recipe));
    }

    private void update(RecipeDto recipeDto, Recipe recipe) {
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPortion(recipeDto.getPortion());
        List<Category> categoryList = recipeDtoMapper.getCategoryList(recipeDto);
        recipe.getIngredients().clear();
        recipeDtoMapper.getIngredients(recipeDto, recipe)
                .forEach(recipe::addIngredientAmount);
        recipe.setCategories(categoryList);
        recipe.setPreparation(recipeDto.getPreparation());
        recipe.setHints(recipeDto.getHints());
        recipe.setImg(recipeDto.getImg());
    }

    public List<RecipeDto> findAllByIdInWhenPublicOrPrivateAndAddedByEmailIs(List<Long> ids) {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByIdInWhenPublicOrPrivateAndAddedByEmailIs(ids, getEmail()));
    }

    public List<RecipeDto> findAllPublicAddedByUser(String nickname) {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByAddedByNicknameAndNonPublicIsFalse(nickname));
    }

    public List<RecipeDto> findAllPrivateNewestFirst() {
        return recipeDtoMapper.maptoDtos(recipeRepository.findAllByNonPublicIsTrueAndAddedByEmailIsOrderByDateAddedDesc(getEmail()));
    }
}