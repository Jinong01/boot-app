package kor.it.academy.user.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class User {

    @Getter@Setter
    public static class Request {

        private String userId;
        private String passwd;
        private String userName;
        private String userBirth;
        private String gender;
        private String mobileNumber;
        private String email;
        private String addr;
        private String addrDetail;
        private String userAuth;
    }

    @Builder@Getter@NoArgsConstructor@AllArgsConstructor
    public static class Response extends Request{

        private String useYn;
        private String delYn;
        private List<UserAuth> authList;
        LocalDateTime createDate;
        LocalDateTime updateDate;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserAuth{
        private String authId;
        private String authName;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserAuthMapping{
        private int seq;
        private String userId;
        private String authId;
    }

}
