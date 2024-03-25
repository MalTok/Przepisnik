package pl.mt.cookbook.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.UserService;

import java.util.Optional;

public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {
    private final UserService userService;

    public UniqueNicknameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueNickname constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> userOptional = userService.findByNickname(nickname);
        return userOptional.isEmpty();
    }
}
