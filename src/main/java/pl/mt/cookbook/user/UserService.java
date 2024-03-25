package pl.mt.cookbook.user;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.dto.*;
import pl.mt.cookbook.user.mapper.UserAddDtoMapper;
import pl.mt.cookbook.user.mapper.UserCredentialsDtoMapper;
import pl.mt.cookbook.user.mapper.UserDtoMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserAddDtoMapper userAddDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper, UserAddDtoMapper userAddDtoMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.userAddDtoMapper = userAddDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean save(UserAddDto userAddDto) {
        if (
                userAddDto.getEmail() == null || userAddDto.getPassword() == null ||
                        userAddDto.getEmail().isEmpty() || userAddDto.getPassword().isEmpty()
        ) {
            return false;
        }
        User user = userAddDtoMapper.map(userAddDto);
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
    public void updateData(UserEditPersonalDto userEditPersonalDto) {
        Optional<User> usernameOptional = findByEmail(userEditPersonalDto.getEmail());
        if (usernameOptional.isPresent()) {
            User user = usernameOptional.get();
            user.setFirstName(userEditPersonalDto.getFirstName());
            user.setLastName(userEditPersonalDto.getLastName());
            user.setBirthDate(userEditPersonalDto.getBirthDate());
        }
    }

    @Transactional
    public void updatePassword(UserEditPasswordDto userEditPasswordDto) {
        Optional<User> usernameOptional = findByEmail(userEditPasswordDto.getEmail());
        if (usernameOptional.isPresent()) {
            User user = usernameOptional.get();
            String encodedPassword = passwordEncoder.encode(userEditPasswordDto.getPassword());
            user.setPassword(encodedPassword);
        }
    }

    @Transactional
    public void updateNewsletter(UserEditNewsletterDto userEditNewsletterDto) {
        Optional<User> usernameOptional = findByEmail(userEditNewsletterDto.getEmail());
        if (usernameOptional.isPresent()) {
            User user = usernameOptional.get();
            user.setNewsletter(userEditNewsletterDto.isNewsletter());
        }
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
