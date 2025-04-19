package study.my_studyolle2.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.my_studyolle2.account.domain.Account;
import study.my_studyolle2.account.domain.Role;
import study.my_studyolle2.account.dto.SignUpForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Account signUp(SignUpForm form) {
        Account newAccount = saveNewAccount(form);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm form) {
        Account account = Account.builder()
                .email(form.getEmail())
                .nickname(form.getNickname())
                .password(passwordEncoder.encode(form.getPassword()))
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();

        return accountRepository.save(account);
    }

    private void sendSignUpConfirmEmail(Account account) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("스터디올래, 회원 가입 인증"); //제목 설정
        mailMessage.setText("/check-email-token?token="+account.getEmailCheckToken()+
                "&email="+account.getEmail());

        mailSender.send(mailMessage);
    }

    public Long updateCheckedEmailToken(Long accountId) {
        Account account = accountRepository.findById(accountId).get();
        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());

        long count = accountRepository.count();
        return count;
    }

//    public void login(Account account) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account.getNickname(),
//                account.getPassword(),
//                List.of(new SimpleGrantedAuthority("USER")));
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(token);
//    }

    public void login(String nickname, String rawPassword) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(nickname, rawPassword);
        Authentication authentication = authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void loginWithoutPassword(Account account) {
        UserDetails userDetails = User.builder()
                .username(account.getNickname())
                .password(account.getPassword()) //어짜피 null 처리됨
                .authorities(Role.USER.name())
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
