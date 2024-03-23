package study.security_nopassword.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username; //이걸로 로그인한다고 가정, 이메일인지 확인

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String mobileCheckNumber;

    public User(String password) {
        this.username = "username";
        this.password = password;
        this.nickname = "nickname";
        this.mobile = "01012341234";
        this.mobileCheckNumber = "123456";
    }
}