package kor.it.academy.login.vo;

import lombok.*;

import java.util.List;

public class LoginUser {

    @Builder@Getter@Setter@NoArgsConstructor@AllArgsConstructor
    public static class LoginRequest {
        private String userId;
        private String passwd;
    }

    @Builder@Getter@NoArgsConstructor@AllArgsConstructor
    public static class UserInfo{
        private String userId;
        private String userName;
        private String passwd;
        private List<UserAuth> authList;
        private boolean isAuth;
    }

    @Builder@Getter@NoArgsConstructor@AllArgsConstructor
    public static class UserAuth{
        private String authId;
        private String authName;
    }
}
