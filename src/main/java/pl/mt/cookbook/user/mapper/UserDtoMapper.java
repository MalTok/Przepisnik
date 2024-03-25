package pl.mt.cookbook.user.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.Role;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRole;
import pl.mt.cookbook.user.dto.UserDto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDtoMapper {
    private final PasswordEncoder passwordEncoder;

    public UserDtoMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setNewsletter(user.isNewsletter());
        userDto.setRoles(user.getRoles().stream().map(userRole -> userRole.getRole().name()).collect(Collectors.toSet()));
        return userDto;
    }

    public User map(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setNewsletter(userDto.isNewsletter());
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(user, Role.ROLE_USER));
        user.setRoles(roles);
        return user;
    }
}
