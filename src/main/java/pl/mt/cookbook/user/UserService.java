package pl.mt.cookbook.user;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.dto.UserCredentialsDto;
import pl.mt.cookbook.user.dto.UserDto;
import pl.mt.cookbook.user.dto.UserEditDto;
import pl.mt.cookbook.user.mapper.UserCredentialsDtoMapper;
import pl.mt.cookbook.user.mapper.UserDtoMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean save(UserDto userDto) {
        if (
                userDto.getEmail() == null || userDto.getPassword() == null ||
                        userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty()
        ) {
            return false;
        }
        User user = userDtoMapper.map(userDto);
        userRepository.save(user);
        return true;
    }

    public List<UserDto> findAllButCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getEmail().equals(currentUser.getName()))
                .map(userDtoMapper::mapToDto)
                .toList();
    }

    @Transactional
    public boolean update(UserEditDto userEditDto) {
        Optional<User> usernameOptional = findByEmail(userEditDto.getEmail());
        if (usernameOptional.isPresent()) {
            User user = usernameOptional.get();
            user.setFirstName(userEditDto.getFirstName());
            user.setLastName(userEditDto.getLastName());
            String encodedPassword = passwordEncoder.encode(userEditDto.getPassword());
            user.setPassword(encodedPassword);
            user.setNewsletter(userEditDto.isNewsletter());
            return true;
        }
        return false;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }

    @Transactional
    public void makeAdmin(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    if (user.getRoles().stream().noneMatch(role -> role.getRole().equals(Role.ROLE_ADMIN))) {
                        user.getRoles().add(new UserRole(user, Role.ROLE_ADMIN));
                    }
                });
    }

    @Transactional
    public void removeAdmin(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            Set<UserRole> roles = user.getRoles();
            roles.removeIf(role -> role.getRole().equals(Role.ROLE_ADMIN));
        }
    }
}
