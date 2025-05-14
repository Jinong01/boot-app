package kor.it.academy.login.service;


import kor.it.academy.common.vo.SecureUser;
import kor.it.academy.login.mapper.LoginMapper;
import kor.it.academy.login.vo.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginDetailService implements UserDetailsService {

    private final LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUser.LoginRequest request = LoginUser.LoginRequest
                                .builder().userId(username).build();

        LoginUser.UserInfo user = null;
        try{
            user = loginMapper.login(request);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new SecureUser(user);
    }
}
