package pl.mt.cookbook.user;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mt.cookbook.user.dto.UserEditDto;
import pl.mt.cookbook.user.mapper.UserEditDtoMapper;

import java.util.Optional;

@RequestMapping("/user")
@Controller
class UserController {
    private final UserService userService;
    private final UserEditDtoMapper userEditDtoMapper;

    public UserController(UserService userService, UserEditDtoMapper userEditDtoMapper) {
        this.userService = userService;
        this.userEditDtoMapper = userEditDtoMapper;
    }

    @GetMapping("/update")
    public String update(@CurrentSecurityContext(expression = "authentication.name") String username, Model model) {
        Optional<User> usernameOptional = userService.findByEmail(username);
        if (usernameOptional.isPresent()) {
            User user = usernameOptional.get();
            UserEditDto userEditDto = userEditDtoMapper.maptoDto(user);
            model.addAttribute("user", userEditDto);
        }
        return "user-form-edit";
    }

    @PostMapping("/update")
    public String update(UserEditDto userEditDto, RedirectAttributes redirectAttributes) {
        userService.update(userEditDto);
        redirectAttributes.addFlashAttribute("updateMessage", true);
        return "redirect:/user/update";
    }
}
