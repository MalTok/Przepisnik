package pl.mt.cookbook.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class ProperPasswordValidator implements ConstraintValidator<ProperPassword, String> {
    @Override
    public void initialize(ProperPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        List<String> errorMessages = new ArrayList<>();

        if (!password.matches(".*[a-ząćęłńóśźż].*")) {
            errorMessages.add("Min. 1 mała litera");
        }

        if (!password.matches(".*[A-ZĄĆĘŁŃÓŚŹŻ].*")) {
            errorMessages.add("Min. 1 duża litera");
        }

        if (!password.matches(".*[0-9].*")) {
            errorMessages.add("Min. 1 cyfra");
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            errorMessages.add("Min. 1 znak specjalny");
        }

        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            errorMessages.forEach(message -> context.buildConstraintViolationWithTemplate(message).addConstraintViolation());
            return false;
        }

        return true;
    }
}
