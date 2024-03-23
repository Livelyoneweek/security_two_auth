package study.security_nopassword.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

    public static class Req {

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Join {

            private String username;

            private String password;

            private String nickname;

            private String mobile;

        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Login {
            private String username;
            private String password;
            private String mobileCheckNumber;
        }
    }

    public static class Res {
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Join {
            private String username;
            private String password;
            private String nickname;
            private String mobile;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class User {
            private Long userId;
            private String username;
            private String nickname;
            private String mobile;
            private Long positionCode;
            private String position;
            private Long authCode;
            private String auth;

            private String time;

            public User(study.security_nopassword.user.entity.User user) {
                this.userId = user.getUserId();
                this.username = user.getUsername();
                this.nickname = user.getNickname();
                this.mobile = user.getMobile();
            }
        }
    }
}
