package kor.it.academy.user.service;

import kor.it.academy.common.vo.PagingVo;
import kor.it.academy.user.mapper.UserMapper;
import kor.it.academy.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PagingVo pagingVo;

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

    public Map<String , Object> getUserList(Map<String , Object> params) throws Exception{
        Map<String , Object> result = new HashMap<>();

        int total = userMapper.getUserListTotal(params);
        int currentPage = (int)params.get("currentPage");
        List<User.Response> userList = new ArrayList<>();

        if (total > 0) {
            pagingVo.setData(currentPage, total);
            params.put("offset", pagingVo.getOffSet());
            params.put("count", pagingVo.getCount());

            userList = userMapper.getUserList(params);
        }
        result.put("userList", userList);
        result.put("pageHtml", pagingVo.pageHtml());
        result.put("total", total);
        result.put("currentPage", currentPage);
        return result;
    }

    public User.Response getUser(String userId) throws Exception{
        return userMapper.getUser(userId);
    }

    @Transactional
    public Map<String, Object> deleteUser(String userList) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String[] deleteUsers = userList.split("#");

        int resultCode = userMapper.deleteUser(deleteUsers);
        if(resultCode < 1) {
            throw new Exception("사용자 삭제 실패");
        }

        resultMap.put("resultCode", 200);
        return resultMap;
    }
}
