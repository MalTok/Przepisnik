package pl.mt.cookbook.user.mapper;

import org.springframework.stereotype.Service;
import pl.mt.cookbook.user.User;
import pl.mt.cookbook.user.dto.UserEditPasswordDto;

@Service
public class UserEditPasswordDtoMapper {
    public UserEditPasswordDto maptoDto(User user) {
        UserEditPasswordDto userEditPasswordDto = new UserEditPasswordDto();
        userEditPasswordDto.setEmail(user.getEmail());
        userEditPasswordDto.setPassword(user.getPassword());
        return userEditPasswordDto;
    }
}
