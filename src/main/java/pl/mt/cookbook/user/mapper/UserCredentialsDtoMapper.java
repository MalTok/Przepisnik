package pl.mt.cookbook.user.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRole;
import pl.mt.cookbook.user.dto.UserCredentialsDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserCredentialsDtoMapper {
    public UserCredentialsDto map(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = getRoles(user);
        return new UserCredentialsDto(email, password, roles);
    }

    private Set<String> getRoles(User user) {
        return user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
