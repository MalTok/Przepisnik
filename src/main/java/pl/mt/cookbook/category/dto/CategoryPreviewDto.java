package pl.mt.cookbook.category.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryPreviewDto {
    private String name;

    private String description;

    private String img;

    private List<Long> recipeIds;
}
