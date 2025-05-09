package kor.it.academy.user.service;

import kor.it.academy.user.mapper.UserMapper;
import kor.it.academy.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Map<String , Object> addUser(User.Request userRequest) throws Exception{
        Map<String , Object> result = new HashMap<>();

        //비밀번호 암호화
        String encodePasswd = passwordEncoder.encode(userRequest.getPasswd());
        //암호화된 패스워드로 변경
        userRequest.setPasswd(encodePasswd);
        int resultCode = userMapper.addUser(userRequest);

        if(resultCode < 1) {
            throw new Exception("회원가입 실패");
        }

        //권한 매핑 등록
        User.UserAuthMapping userAuthMapping =
                User.UserAuthMapping.builder()
                        .authId(userRequest.getUserAuth())
                        .userId(userRequest.getUserId())
                        .build();

        userMapper.addUserAuthMapping(userAuthMapping);

        result.put("resultCode", 200);
        return result;
    }

    public List<User.UserAuth> getUserAuthList() throws Exception{
        return userMapper.getUserAuthList();
    }
}
