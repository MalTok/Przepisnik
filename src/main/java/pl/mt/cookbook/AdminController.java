package pl.mt.cookbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mt.cookbook.user.UserService;
import pl.mt.cookbook.user.dto.UserDto;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/show-all")
    public String showAllUsers(Model model) {
        List<UserDto> userDtoList = userService.findAllButCurrentUser();
        if (userDtoList.isEmpty()) {
            model.addAttribute("message", true);
        }
        model.addAttribute("users", userDtoList);
        return "users";
    }

    @PostMapping("/add/{email}")
    public String addRoleAdmin(@PathVariable String email) {
        userService.makeAdmin(email);
        return "redirect:/admin/show-all";
    }

    @PostMapping("/remove/{email}")
    public String removeRoleAdmin(@PathVariable String email) {
        userService.removeAdmin(email);
        return "redirect:/admin/show-all";
    }
}
