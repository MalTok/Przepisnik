package pl.mt.cookbook.category.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.category.dto.CategoryAddDto;

@Service
public class CategoryAddDtoMapper {
    public Category map(CategoryAddDto categoryAddDto) {
        Category category = new Category();
        category.setName(categoryAddDto.getName());
        category.setDescription(categoryAddDto.getDescription());
        category.setImg(categoryAddDto.getImg());
        String url = createUrl(categoryAddDto.getName());
        category.setUrl(url);
        return category;
    }

    public CategoryAddDto mapToAddDto(Category category) {
        CategoryAddDto categoryAddDto = new CategoryAddDto();
        categoryAddDto.setName(category.getName());
        categoryAddDto.setDescription(category.getDescription());
        categoryAddDto.setImg(category.getImg());
        categoryAddDto.setUrl(category.getUrl());
        return categoryAddDto;
    }

    private String createUrl(String name) {
        return name
                .toLowerCase()
                .replaceAll(" ", "-");
    }
}
