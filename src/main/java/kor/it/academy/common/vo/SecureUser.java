package kor.it.academy.common.vo;

import kor.it.academy.login.vo.LoginUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;
import java.util.List;

public class SecureUser extends User {

    private static final long serialVersionID = 1L;
    //시큐리티한테 권한이름 줄 때 ROLE_ 을 꼭 붙혀야한다
    private static final String ROLE_PREFIX = "ROLE_";

    @Getter
    private String userId;
    @Getter
    private String userName;
    @Getter
    private String userAuth;
    @Getter
    private boolean isAuth;

    public SecureUser(LoginUser.UserInfo user){
        super(user.getUserName(), user.getPasswd(), makeAuthList(user.getAuthList()));
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userAuth = user.getAuthList().get(0).getAuthId();
        this.isAuth = userAuth.equals("ADMIN") ? true : false;
    }
    private static List<GrantedAuthority> makeAuthList(List<LoginUser.UserAuth> authList){
        List<GrantedAuthority> list = new ArrayList<>();

        for (LoginUser.UserAuth auth : authList){
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX + auth.getAuthId()));
        }
        return list;
    }
}
