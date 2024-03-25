package pl.mt.cookbook.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mt.cookbook.recipe.Recipe;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String img;
    @ManyToMany(mappedBy = "categories", cascade = CascadeType.REMOVE)
    private List<Recipe> recipes;
    @Column(unique = true)
    private String url;

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
}