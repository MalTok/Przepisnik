package pl.mt.cookbook;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.mt.cookbook.category.Category;
import pl.mt.cookbook.category.dto.CategoryAddDto;
import pl.mt.cookbook.category.mapper.CategoryAddDtoMapper;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRole;
import pl.mt.cookbook.user.dto.UserCredentialsDto;
import pl.mt.cookbook.user.mapper.UserCredentialsDtoMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CookbookAppTests {
    @Test
    void shouldReturnProperUrlForOneWordName() {
        //given
        CategoryAddDtoMapper categoryAddDtoMapper = new CategoryAddDtoMapper();
        CategoryAddDto categoryAddDtoMock = Mockito.mock(CategoryAddDto.class);
        Mockito.when(categoryAddDtoMock.getName()).thenReturn("Lunche");

        //when
        Category mapped = categoryAddDtoMapper.map(categoryAddDtoMock);

        //then
        assertThat(mapped.getUrl()).isEqualTo("lunche");
    }

    @Test
    void shouldReturnProperUrlForMultiWordName() {
        //given
        CategoryAddDtoMapper categoryAddDtoMapper = new CategoryAddDtoMapper();
        CategoryAddDto categoryAddDtoMock = Mockito.mock(CategoryAddDto.class);
        Mockito.when(categoryAddDtoMock.getName()).thenReturn("Z jednego garnka");

        //when
        Category mapped = categoryAddDtoMapper.map(categoryAddDtoMock);

        //then
        assertThat(mapped.getUrl()).isEqualTo("z-jednego-garnka");
    }

    @Test
    void shouldReturnProperRoleString() {
        //given
        UserCredentialsDtoMapper userCredentialsDtoMapper = new UserCredentialsDtoMapper();

        User userMock = Mockito.mock(User.class);
        Set<UserRole> roles = new HashSet<>(List.of(
                new UserRole(userMock, pl.mt.cookbook.user.Role.ROLE_USER)
        ));

        Mockito.when(userMock.getRoles()).thenReturn(roles);

        //when
        UserCredentialsDto userCredentialsDto = userCredentialsDtoMapper.map(userMock);

        //then
        assertThat(userCredentialsDto.getRoles()).contains("ROLE_USER");
    }

}
