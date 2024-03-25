package pl.mt.cookbook.user;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mt.cookbook.user.dto.UserEditNewsletterDto;
import pl.mt.cookbook.user.dto.UserEditPasswordDto;
import pl.mt.cookbook.user.dto.UserEditPersonalDto;
import pl.mt.cookbook.user.mapper.UserEditNewsletterDtoMapper;
import pl.mt.cookbook.user.mapper.UserEditPasswordDtoMapper;
import pl.mt.cookbook.user.mapper.UserEditPersonalDtoMapper;

import java.util.Optional;

@RequestMapping("/user")
@Controller
class UserController {
    private final UserService userService;
    private final UserEditPersonalDtoMapper userEditPersonalDtoMapper;
    private final UserEditPasswordDtoMapper userEditPasswordDtoMapper;
    private final UserEditNewsletterDtoMapper userEditNewsletterDtoMapper;

    public UserController(UserService userService, UserEditPersonalDtoMapper userEditPersonalDtoMapper,
                          UserEditPasswordDtoMapper userEditPasswordDtoMapper,
                          UserEditNewsletterDtoMapper userEditNewsletterDtoMapper) {
        this.userService = userService;
        this.userEditPersonalDtoMapper = userEditPersonalDtoMapper;
        this.userEditPasswordDtoMapper = userEditPasswordDtoMapper;
        this.userEditNewsletterDtoMapper = userEditNewsletterDtoMapper;
    }

    @ModelAttribute(name = "userPersonal")
    UserEditPersonalDto userEditPersonalDto(@CurrentSecurityContext(expression = "authentication.name") String username) {
        Optional<User> usernameOptional = userService.findByEmail(username);
        return usernameOptional.map(userEditPersonalDtoMapper::maptoDto).orElseGet(UserEditPersonalDto::new);
    }

    @ModelAttribute(name = "userPassword")
    UserEditPasswordDto userEditPasswordDto(@CurrentSecurityContext(expression = "authentication.name") String username) {
        Optional<User> usernameOptional = userService.findByEmail(username);
        return usernameOptional.map(userEditPasswordDtoMapper::maptoDto).orElseGet(UserEditPasswordDto::new);
    }

    @ModelAttribute(name = "userNewsletter")
    UserEditNewsletterDto userEditNewsletterDto(@CurrentSecurityContext(expression = "authentication.name") String username) {
        Optional<User> usernameOptional = userService.findByEmail(username);
        return usernameOptional.map(userEditNewsletterDtoMapper::maptoDto).orElseGet(UserEditNewsletterDto::new);
    }

    @GetMapping("/update")
    public String update() {
        return "user-form-edit";
    }

    @PostMapping("/update/personal-data")
    public String updateData(@Valid @ModelAttribute("userPersonal") UserEditPersonalDto userEditPersonalDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userPersonal", userEditPersonalDto);
            return "user-form-edit";
        } else {
            userService.updateData(userEditPersonalDto);
            redirectAttributes.addFlashAttribute("updateMessage", true);
            return "redirect:/user/update";
        }
    }

    @PostMapping("/update/password")
    public String updatePassword(@Valid @ModelAttribute("userPassword") UserEditPasswordDto userEditPasswordDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userPassword", userEditPasswordDto);
            return "user-form-edit";
        } else {
            userService.updatePassword(userEditPasswordDto);
            redirectAttributes.addFlashAttribute("updateMessage", true);
            return "redirect:/user/update";
        }
    }

    @PostMapping("/update/newsletter")
    public String updateNewsletter(@Valid @ModelAttribute("userNewsletter") UserEditNewsletterDto userEditNewsletterDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userNewsletter", userEditNewsletterDto);
            return "user-form-edit";
        } else {
            userService.updateNewsletter(userEditNewsletterDto);
            redirectAttributes.addFlashAttribute("updateMessage", true);
            return "redirect:/user/update";
        }
    }
}
