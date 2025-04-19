package study.my_studyolle2.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.my_studyolle2.account.CurrentUser;
import study.my_studyolle2.account.domain.Account;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if(account != null) {
            model.addAttribute("account", account);
        }
        return "index";
    }
}
