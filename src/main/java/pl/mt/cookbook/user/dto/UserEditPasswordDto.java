package pl.mt.cookbook.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.mt.cookbook.validation.ProperPassword;

@Data
public class UserEditPasswordDto {
    private String email;
    @NotBlank
    @Size(min = 8, max = 255, message = "Hasło musi mieć minimalną długość {min} znaków")
    @ProperPassword
    private String password;
}
