package pl.mt.cookbook.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByTitleContainingIgnoreCase(String word);

    @Query("SELECT r FROM Recipe r ORDER BY r.likes DESC ")
    List<Recipe> findAllSortedByLikes();

    List<Recipe> findAllByIdIn(List<Long> ids);

    List<Recipe> findAllByAddedByNickname(String nickname);
}