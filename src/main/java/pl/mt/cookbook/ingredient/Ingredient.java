package pl.mt.cookbook.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mt.cookbook.recipe.IngredientAmount;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientAmount> ingredientAmounts = new ArrayList<>();

    public void addIngredientAmount(IngredientAmount ingredientAmount) {
        ingredientAmounts.add(ingredientAmount);
    }
}