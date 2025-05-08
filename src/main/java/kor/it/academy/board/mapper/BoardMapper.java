package kor.it.academy.board.mapper;

import kor.it.academy.board.vo.BoardData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    //전체 게시글 가져오기
    int getBoardTotal() throws SQLException;

    //화면에 보여줄 게시글리스트 가져오기
    List<BoardData.Response> getBoardList(Map<String, Object> param) throws SQLException;

    //게시글 작성
    int addBoard(BoardData.Request request) throws SQLException;

    //파일 첨부
    int addFile(BoardData.BoardFiles boardFile) throws SQLException;

    //게시글 상세 가져오기
    BoardData.Detail getBoardDetail(@Param("seq")int seq) throws SQLException;

    BoardData.BoardFiles getBoardFile(@Param("seq")int seq) throws SQLException;

    //게시글 수정
    int updateBoard(BoardData.Request request) throws SQLException;

    //게시글 삭제
    int deleteBoard(@Param("seq")int seq) throws SQLException;

    //파일 삭제
    int deleteFile(@Param("seq")int seq) throws SQLException;

    //게시글 조회 수 증가
    void updateReadCount(@Param("seq")int seq) throws SQLException;
}
