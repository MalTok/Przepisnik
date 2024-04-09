package pl.mt.cookbook.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByTitleContainingIgnoreCaseAndNonPublicIsFalse(String word);

    @Query("SELECT r FROM Recipe r WHERE r.nonPublic = false ORDER BY SIZE(r.users) DESC")
    List<Recipe> findAllPublicSortedByLikes();

    @Query(value = "SELECT * FROM recipe " +
            "WHERE " +
            "(id in ?1 AND non_public = false) " +
            "OR " +
            "(id in ?1 AND non_public = true AND added_by_email = ?2)", nativeQuery = true)
    List<Recipe> findAllByIdInWhenPublicOrPrivateAndAddedByEmailIs(List<Long> ids, String email);

    List<Recipe> findAllByAddedByNicknameAndNonPublicIsFalse(String nickname);

    List<Recipe> findAllByNonPublicIsTrueAndAddedByEmailIsOrderByDateAddedDesc(String email);

    List<Recipe> findAllByNonPublicIsFalse();

    @Query(value = "SELECT * FROM recipe " +
            "WHERE " +
            "(LOWER(title) like LOWER(CONCAT('%', ?1, '%')) AND non_public = false) " +
            "OR " +
            "(LOWER(title) like LOWER(CONCAT('%', ?1, '%')) AND non_public = true AND added_by_email = ?2)", nativeQuery = true)
    List<Recipe> findAllByTitleIgnoreCaseIsWhenPublicOrPrivateAndAddedByEmailIs(String word, String email);
}