package pl.mt.cookbook.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.mt.cookbook.validation.ProperPassword;

import java.util.Date;
import java.util.Set;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    @NotBlank
    @Size(min = 2, max = 255)
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") // RFC 5322
    private String email;
    @NotBlank
    @Size(min = 8, max = 255, message = "Hasło musi mieć minimalną długość {min} znaków")
//    alternatywnie: @Pattern(regexp = "^(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*[0-9])(?=.*[@#$%^&+=!]).*$",
//            message = "Hasło musi zawierać min. 1 małą i 1 dużą literę, 1 znak specjalny, 1 cyfrę")
    @ProperPassword
    private String password;
    private boolean newsletter;
    private Set<String> roles;
}
