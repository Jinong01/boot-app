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

    @Transactional
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

        int result = userMapper.deleteUser(deleteUsers);
        if(result < 1) {
            throw new Exception("사용자 삭제 실패");
        }

        resultMap.put("resultCode", 200);
        return resultMap;
    }

    @Transactional
    public Map<String, Object> updateUser(User.Request userRequest) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();

        //기존 유저부르기
        User.Response user = userMapper.getUser(userRequest.getUserId());

        //비밀번호가 변경되지 않았으면 기존 꺼 사용
        if(userRequest.getPasswd() == null && userRequest.getPasswd().isBlank()) {
            userRequest.setPasswd(passwordEncoder.encode(user.getPasswd()));
        } else {
            //변경되었으면 새로운걸 인코딩해서 변경
            userRequest.setPasswd(passwordEncoder.encode(userRequest.getPasswd()));
        }

        //사용자 정보 수정
        int resultCode = userMapper.updateUser(userRequest);
        resultCode += userMapper.updateUserAuth(userRequest);
        if (resultCode < 2) {
            throw new Exception("회원 정보 수정 실패");
        }

        resultMap.put("resultCode", 200);
        return resultMap;
    }
}
