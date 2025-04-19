package study.my_studyolle2.account.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id") //순환참조 방지를 위해 id만 equals 구현
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio; //자기소개

    private String url;

    private String occupation; //직업

    private String location;

//    // todo
//    @Enumerated(EnumType.STRING)
//    private Role role;

    @Lob @Basic
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public void generateEmailCheckToken() {
        this.emailCheckToken= UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token) {
        return this.getEmailCheckToken().equals(token);
    }
}
