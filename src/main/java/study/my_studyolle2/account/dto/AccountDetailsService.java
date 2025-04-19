package study.my_studyolle2.account.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.my_studyolle2.account.AccountRepository;
import study.my_studyolle2.account.domain.Account;
import study.my_studyolle2.account.domain.Role;

//UserDetailsService는 이 username으로 db에 가서 진짜 정보(비밀번호, 권한) 등을 가져오는 역할을 한다.
@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByNickname(username)
                        .orElseThrow(()->new UsernameNotFoundException("user not found"));

        return User.builder()
                .username(account.getNickname())
                .password(account.getPassword()) //암호화된 비밀번호
                .authorities(Role.USER.name())
                .build();
    }
}
