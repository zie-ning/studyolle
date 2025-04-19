package study.my_studyolle2.account;

import org.springframework.data.jpa.repository.JpaRepository;
import study.my_studyolle2.account.domain.Account;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Account findByEmail(String email);
    Optional<Account> findByNickname(String nickname);
}
