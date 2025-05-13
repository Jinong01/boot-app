package kor.it.academy.user.mapper;

import kor.it.academy.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int addUser(User.Request userRequest) throws SQLException;

    //권한리스트
    List<User.UserAuth> getUserAuthList() throws SQLException;

    //권한 매핑 추가
    int addUserAuthMapping(User.UserAuthMapping userAuthMapping) throws SQLException;

    //사용자 전체 카운트
    int getUserListTotal(Map<String, Object> params) throws SQLException;

    //사용자 리스트
    List<User.Response> getUserList(Map<String , Object> param) throws SQLException;

    //권한매핑 삭제
    int deleteAuthMapping(@Param("deleteUsers")String[] deleteUsers) throws SQLException;

    //사용자 삭제
    int deleteUser (@Param("deleteUsers")String[] deleteUsers) throws SQLException;

    User.Response getUser(@Param("userId") String userId) throws SQLException;

    int updateUser(User.Request userRequest) throws SQLException;

    int updateUserAuth(User.Request userRequest) throws SQLException;
}
