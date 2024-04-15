package pl.mt.cookbook.category;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.category.dto.CategoryAddDto;
import pl.mt.cookbook.category.dto.CategoryMenuDto;
import pl.mt.cookbook.category.dto.CategoryPreviewDto;
import pl.mt.cookbook.category.mapper.CategoryAddDtoMapper;
import pl.mt.cookbook.category.mapper.CategoryMenuDtoMapper;
import pl.mt.cookbook.category.mapper.CategoryPreviewDtoMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryAddDtoMapper categoryAddDtoMapper;
    private final CategoryPreviewDtoMapper categoryPreviewDtoMapper;
    private final CategoryMenuDtoMapper categoryMenuDtoMapper;

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryAddDtoMapper categoryAddDtoMapper,
            CategoryPreviewDtoMapper categoryPreviewDtoMapper,
            CategoryMenuDtoMapper categoryMenuDtoMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryAddDtoMapper = categoryAddDtoMapper;
        this.categoryPreviewDtoMapper = categoryPreviewDtoMapper;
        this.categoryMenuDtoMapper = categoryMenuDtoMapper;
    }

    public List<CategoryMenuDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMenuDtoMapper::mapToMenuDto)
                .toList();
    }

    public CategoryPreviewDto findByUrl(String url) {
        Optional<Category> categoryOptional = categoryRepository.findByUrl(url);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return categoryPreviewDtoMapper.mapToPreviewDto(category);
        } else {
            throw new NoSuchElementException("Category doesn't exists.");
        }
    }

    @Transactional
    public CategoryAddDto save(CategoryAddDto categoryAddDto) {
        String name = categoryAddDto.getName();
        Optional<Category> categoryOptional = categoryRepository.findByNameIgnoreCase(name);
        if (categoryOptional.isPresent()) {
            return categoryAddDtoMapper.mapToAddDto(categoryOptional.get());
        } else {
            Category category = categoryAddDtoMapper.map(categoryAddDto);
            categoryRepository.save(category);
            return categoryAddDtoMapper.mapToAddDto(category);
        }
    }

    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }
}