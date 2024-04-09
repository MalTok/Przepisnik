package pl.mt.cookbook.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int portion;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<IngredientAmount> ingredients = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String preparation;

    private String hints;

    private String img;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateAdded;

    private String addedByNickname;

    private String addedByEmail;

    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    @ManyToMany
    private List<User> users;

    private boolean nonPublic;

    public void addCategory(Category category) {
        categories.add(category);
        category.addRecipe(this);
    }

    public void addIngredientAmount(IngredientAmount ingredientAmount) {
        ingredients.add(ingredientAmount);
    }
}