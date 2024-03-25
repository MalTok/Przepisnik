package pl.mt.cookbook.user.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.dto.UserEditPersonalDto;

@Service
public class UserEditPersonalDtoMapper {
    public UserEditPersonalDto maptoDto(User user) {
        UserEditPersonalDto userEditPersonalDto = new UserEditPersonalDto();
        userEditPersonalDto.setEmail(user.getEmail());
        userEditPersonalDto.setFirstName(user.getFirstName());
        userEditPersonalDto.setLastName(user.getLastName());
        userEditPersonalDto.setBirthDate(user.getBirthDate());
        return userEditPersonalDto;
    }
}
