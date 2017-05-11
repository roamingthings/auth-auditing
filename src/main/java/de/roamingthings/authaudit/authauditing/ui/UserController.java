package de.roamingthings.authaudit.authauditing.ui;

import de.roamingthings.authaudit.authauditing.domain.User;
import de.roamingthings.authaudit.authauditing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/11
 */
@Controller
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/users")
    public String listUsers(Model model) {
        final List<User> allUsers = userRepository.findAll();

        model.addAttribute("users", allUsers);
        return "users_list.html";
    }
}
