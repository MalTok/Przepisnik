package pl.mt.cookbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mt.cookbook.user.dto.UserDto;
import pl.mt.cookbook.user.UserService;

@Controller
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String addForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user-form";
    }

    @PostMapping("/register")
    public String add(UserDto userDto, RedirectAttributes redirectAttributes) {
        boolean saved = userService.save(userDto);
        if (saved) {
            redirectAttributes.addFlashAttribute("registrationSuccess", true);
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login-form";
    }
}
