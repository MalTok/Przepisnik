package pl.mt.cookbook.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueNicknameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNickname {
    String message() default "Nickname already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
