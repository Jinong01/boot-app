package kor.it.academy.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kor.it.academy.board.mapper.BoardMapper;
import kor.it.academy.board.vo.BoardData;
import kor.it.academy.common.utils.CommonFileUtils;
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

    public BoardData.Detail getBoardDetail(int seq) throws SQLException{
        return boardMapper.getBoardDetail(seq);
    }

    public BoardData.BoardFiles getBoardFile(int seq) throws SQLException{
        return boardMapper.getBoardFile(seq);
    }

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
}
