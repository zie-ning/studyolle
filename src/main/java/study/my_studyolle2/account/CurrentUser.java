package study.my_studyolle2.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
//여기서 'account'는 UserAccount의 account 프로퍼티
//익명사용자로 접근할 경우 principal은 'anonymousUser'이라는 문자열로 반환됨
public @interface CurrentUser {
}
