package pl.mt.cookbook.user.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.dto.UserEditNewsletterDto;

@Service
public class UserEditNewsletterDtoMapper {
    public UserEditNewsletterDto maptoDto(User user) {
        UserEditNewsletterDto userEditNewsletterDto = new UserEditNewsletterDto();
        userEditNewsletterDto.setEmail(user.getEmail());
        userEditNewsletterDto.setNewsletter(user.isNewsletter());
        return userEditNewsletterDto;
    }
}
