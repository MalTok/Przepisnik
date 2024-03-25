package pl.mt.cookbook.user.mapper;

import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRole;
import pl.mt.cookbook.user.dto.UserCredentialsDto;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto map(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .map(Enum::name)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(email, password, roles);
    }
}
