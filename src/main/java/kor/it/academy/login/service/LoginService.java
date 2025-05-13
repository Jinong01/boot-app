package kor.it.academy.login.service;

import jakarta.servlet.http.HttpSession;
import kor.it.academy.login.mapper.LoginMapper;
import kor.it.academy.login.vo.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> login(LoginUser.LoginRequest loginRequest,
                                     HttpSession session) throws Exception {
        Map<String, Object> result = new HashMap<>();

        LoginUser.UserInfo user = loginMapper.login(loginRequest);

        if (user == null ||
                !passwordEncoder.matches(loginRequest.getPasswd(), user.getPasswd())) {
            throw new Exception("로그인 실패");
        }

        //가지고 있는 권한 중 anyMatch(아무거나) ADMIN 이면
        boolean isAuth = user.getAuthList().stream().anyMatch(auth -> auth.getAuthId().equals("ADMIN"));

        //패스워드 숨기기
        LoginUser.UserInfo logined =
                LoginUser.UserInfo.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .authList(user.getAuthList())
                        .isAuth(isAuth)
                        .build();

        //세션 저장
        session.setAttribute("userInfo", logined);
        session.setMaxInactiveInterval(1800);
        result.put("resultCode", 200);
        return result;
    }
}
