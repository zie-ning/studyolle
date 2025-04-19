package study.my_studyolle2.account;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import study.my_studyolle2.account.domain.Account;
import study.my_studyolle2.account.domain.Role;

import java.util.List;

// 스프링 시큐리티가 다루는 user 정보와 우리 domain에서 다루는 user 정보의 gap을 매꿔주는 어댑터
@Getter
public class UserAccount extends User {
    private Account account;

    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority(Role.USER.name())));
        this.account= account;
    }
}
