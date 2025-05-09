package kor.it.academy.user.mapper;

import kor.it.academy.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface UserMapper {

    int addUser(User.Request userRequest) throws SQLException;

    //권한리스트
    List<User.UserAuth> getUserAuthList() throws SQLException;

    //권한 매핑 추가
    int addUserAuthMapping(User.UserAuthMapping userAuthMapping) throws SQLException;
}
