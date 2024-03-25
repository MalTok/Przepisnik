package pl.mt.cookbook.user.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.Role;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserRole;
import pl.mt.cookbook.user.dto.UserAddDto;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserAddDtoMapper {
    private final PasswordEncoder passwordEncoder;

    public UserAddDtoMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User map(UserAddDto userAddDto) {
        User user = new User();
        user.setEmail(userAddDto.getEmail());
        user.setPassword(passwordEncoder.encode(userAddDto.getPassword()));
        user.setNewsletter(false);
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(user, Role.ROLE_USER));
        user.setRoles(roles);
        return user;
    }
}
