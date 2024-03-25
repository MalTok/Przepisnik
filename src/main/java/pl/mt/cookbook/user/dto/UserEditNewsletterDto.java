package pl.mt.cookbook.user.dto;

import lombok.Data;

@Data
public class UserEditNewsletterDto {
    private String email;

    private boolean newsletter;
}
