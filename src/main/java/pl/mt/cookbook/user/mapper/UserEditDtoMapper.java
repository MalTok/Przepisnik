package pl.mt.cookbook.user.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.dto.UserEditDto;

@Service
public class UserEditDtoMapper {
    public UserEditDto maptoDto(User user) {
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setEmail(user.getEmail());
        userEditDto.setFirstName(user.getFirstName());
        userEditDto.setLastName(user.getLastName());
        userEditDto.setPassword(user.getPassword());
        userEditDto.setNewsletter(user.isNewsletter());
        return userEditDto;
    }
}
