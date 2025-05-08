package kor.it.academy.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kor.it.academy.board.mapper.BoardMapper;
import kor.it.academy.board.vo.BoardData;
import kor.it.academy.common.utils.CommonFileUtils;
import kor.it.academy.common.utils.CookieUtils;
import kor.it.academy.common.vo.PagingVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final PagingVo pagingVo;

    @Value("${server.file.path}")
    private String filePath;

    public BoardData.ResultData getBoardList(int currentPage) throws Exception{
        int totalRows = boardMapper.getBoardTotal();
        List<BoardData.Response> dataList = new ArrayList<>();
        Map<String , Object> param = new HashMap<>();
        
        if (totalRows > 0) {
            pagingVo.setData(currentPage, totalRows);
            param.put("offset", pagingVo.getOffSet());
            param.put("count", pagingVo.getCount());

            dataList = boardMapper.getBoardList(param);
        }

        return BoardData.ResultData
                .builder()
                .totalRows(totalRows)
                .list(dataList)
                .currentPage(currentPage)
                .pageHtml(pagingVo.pageHtml()).build();
    }

    //게시글 개별 조회
    public BoardData.Detail getBoardDetail(HttpServletRequest request, HttpServletResponse response, Integer seq) throws SQLException{
        if (!CookieUtils.checkCookie(request, seq.toString())){
            boardMapper.updateReadCount(seq);
            CookieUtils.updateCookie(request, response, seq.toString());
        }
        return boardMapper.getBoardDetail(seq);
    }

    public BoardData.Detail getBoardDetail(int seq) throws SQLException{

        return boardMapper.getBoardDetail(seq);
    }

    //게시글 수정용 내용 조회
    public BoardData.BoardFiles getBoardFile(int seq) throws SQLException{
        return boardMapper.getBoardFile(seq);
    }

    //게시글 작성
    @Transactional
    public Map<String, Object> addBoard(BoardData.Request request) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();

        //게시글 등록
        request.setWriter("관리자");
        int result = boardMapper.addBoard(request);

        if(result < 1){
            throw new SQLException("게시글 등록 실패");
        }

        List<MultipartFile> files = Arrays.asList(request.getFile());
        List<Map<String, Object>> fileMap
                = CommonFileUtils.uploadFiles(files, filePath, "board");

        if(!fileMap.isEmpty()){
            //map to Object 로 변경하기 위한 객체 선언
            ObjectMapper objectMapper = new ObjectMapper();
            for(Map<String, Object> file : fileMap){
                //map 을 BoardFiles 객체로 전환
                BoardData.BoardFiles boardFiles
                        = objectMapper.convertValue(file, BoardData.BoardFiles.class);
                //DB에 등록
                boardFiles.setBoardSeq(request.getSeq());
                boardMapper.addFile(boardFiles);
            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글 등록했습니다.");

        return resultMap;
    }

    //게시글 수정
    @Transactional
    public Map<String, Object> updateBoard(BoardData.Request request) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();

        //기존 게시글 호출
        BoardData.Detail detail = getBoardDetail(request.getSeq());

        //게시글 수정
        int result = boardMapper.updateBoard(request);

        if(result < 1){
            throw new SQLException("게시글 수정 실패");
        }

        //수정할 파일이 있으면 기존 파일 삭제 후 새로 등록
        if(!request.getFile().isEmpty()){
            //파일 삭제
            if (!detail.getFiles().isEmpty()) {
                deleteBoardFile(detail.getFiles());
            }

            List<MultipartFile> files = Arrays.asList(request.getFile());
            List<Map<String, Object>> fileMap
                    = CommonFileUtils.uploadFiles(files, filePath, "board");

            if(!fileMap.isEmpty()){
                //map to Object 로 변경하기 위한 객체 선언
                ObjectMapper objectMapper = new ObjectMapper();
                for(Map<String, Object> file : fileMap){
                    //map 을 BoardFiles 객체로 전환
                    BoardData.BoardFiles boardFiles
                            = objectMapper.convertValue(file, BoardData.BoardFiles.class);
                    //DB에 등록
                    boardFiles.setBoardSeq(request.getSeq());
                    boardMapper.addFile(boardFiles);
                }
            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글 수정했습니다.");

        return resultMap;
    }

    //게시글 삭제
    public Map<String, Object> deleteBoard(int seq) throws SQLException{
        Map<String, Object> resultMap = new HashMap<>();
        //기존 게시글 호출
        BoardData.Detail detail = getBoardDetail(seq);

        //파일 삭제
        if (!detail.getFiles().isEmpty()) {
            deleteBoardFile(detail.getFiles());
        }

        //게시글 삭제
        boardMapper.deleteBoard(seq);

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글을 삭제했습니다.");
        return resultMap;
    }

    //파일삭제 - 게시글 수정, 게시글 삭제에 사용하는 공통 메서드
    private void deleteBoardFile(List<BoardData.BoardFiles> boardFiles) throws SQLException{
        //파일 삭제
        for(BoardData.BoardFiles file : boardFiles){
            String path = file.getFilePath() + file.getFileName();
            CommonFileUtils.deleteFile(path);
            //DB 삭제
            boardMapper.deleteFile(file.getSeq());
        }
    }
}
