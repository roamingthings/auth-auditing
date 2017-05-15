package de.roamingthings.authaudit.authauditing.ui;

import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import de.roamingthings.authaudit.authauditing.repository.UserAccountRepository;
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
    private UserAccountRepository userAccountRepository;

    @Autowired
    public UserController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @RequestMapping("/users")
    public String listUsers(Model model) {
        final List<UserAccount> allUserAccounts = userAccountRepository.findAll();

        model.addAttribute("users", allUserAccounts);
        return "users_list.html";
    }
}
