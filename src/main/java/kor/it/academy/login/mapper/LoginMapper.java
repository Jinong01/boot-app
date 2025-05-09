package kor.it.academy.login.mapper;

import kor.it.academy.login.vo.LoginUser;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;

@Mapper
public interface LoginMapper {

    LoginUser.UserInfo login(LoginUser.LoginRequest loginRequest) throws SQLException;
}
