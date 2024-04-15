package pl.mt.cookbook;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.category.dto.CategoryAddDto;
import pl.mt.cookbook.category.mapper.CategoryAddDtoMapper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CookbookAppTests {
    @Test
    void shouldReturnProperUrlForOneWordName() {
        //given
        CategoryAddDtoMapper categoryAddDtoMapper = new CategoryAddDtoMapper();
        CategoryAddDto categoryAddDto = Mockito.mock(CategoryAddDto.class);
        Mockito.when(categoryAddDto.getName()).thenReturn("Lunche");

        //when
        Category mapped = categoryAddDtoMapper.map(categoryAddDto);

        //then
        assertThat(mapped.getUrl()).isEqualTo("lunche");
    }

    @Test
    void shouldReturnProperUrlForMultiWordName() {
        //given
        CategoryAddDtoMapper categoryAddDtoMapper = new CategoryAddDtoMapper();
        CategoryAddDto categoryAddDto = Mockito.mock(CategoryAddDto.class);
        Mockito.when(categoryAddDto.getName()).thenReturn("Z jednego garnka");

        //when
        Category mapped = categoryAddDtoMapper.map(categoryAddDto);

        //then
        assertThat(mapped.getUrl()).isEqualTo("z-jednego-garnka");
    }

}
