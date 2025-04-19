package study.my_studyolle2.account;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import study.my_studyolle2.account.domain.Account;
import study.my_studyolle2.account.dto.SignUpForm;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    //todo
    //@Valid만으로 처리할 수 없는 부분 해결
    //signUpForm 데이터를 받을 때 binder 설정 가능
    @InitBinder("signUpForm") //이름은 타입(SignUpForm)의 캐멀케이스를 따라감(빈 이름과 동일)
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm,
                               Errors errors, HttpSession session){
//        signUpFormValidator.validate(signUpForm, errors); //@InitBinder로 인해 생략
        if(errors.hasErrors()){
            return "account/sign-up";
        }
        Account account = accountService.signUp(signUpForm);
        accountService.login(account.getNickname(),signUpForm.getPassword()); //자동 로그인

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(@RequestParam("token") String token,
                                  @RequestParam("email") String email, Model model
    ){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if(account == null){
            model.addAttribute("error","wrong.email");
            return view;
        }
        if(!account.isValidToken(token)){
            model.addAttribute("error","wrong.token");
            return view;
        }
        Long count=accountService.updateCheckedEmailToken(account.getId());
        accountService.loginWithoutPassword(account);
        model.addAttribute("numberOfUser",count);
        model.addAttribute("nickname", account.getNickname());
        return view;
    }
}
