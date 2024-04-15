package pl.mt.cookbook.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserCredentialsDto {
    private final String email;

    private final String password;

    private final Set<String> roles;
}
