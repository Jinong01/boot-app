package kor.it.academy.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kor.it.academy.board.entity.BoardEntity;
import kor.it.academy.board.entity.BoardFileEntity;
import kor.it.academy.board.mapper.BoardFileRepository;
import kor.it.academy.board.mapper.BoardRepository;
import kor.it.academy.board.mapper.specification.BoardSpecification;
import kor.it.academy.board.vo.BoardDTO;
import kor.it.academy.board.vo.BoardData;
import kor.it.academy.common.utils.CommonFileUtils;
import kor.it.academy.common.utils.CookieUtils;
import kor.it.academy.common.vo.PagingVo;
import kor.it.academy.common.vo.SecureUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardJPAService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    @Value("${server.file.path}")
    private String filePath;

    public Map<String, Object> getBoardList(int currentPage) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "seq");
        PageRequest pageRequest = PageRequest.of(currentPage, 10, sort);
        Map<String , Object> resultMap = new HashMap<>();

        Page<BoardEntity> pages = boardRepository.findAll(pageRequest);

        PagingVo pageVO = new PagingVo();
        pageVO.setData(currentPage, (int)pages.getTotalElements());

        resultMap.put("pageHtml", pageVO.pageHtml());
        resultMap.put("currentPage", currentPage);
        resultMap.put("list", pages.getContent());
        resultMap.put("totalRows", pages.getTotalElements());

        return resultMap;
    }

    public Map<String , Object> searchBoardList(int currentPage, String searchType, String searchText) throws Exception {
        Map<String , Object> resultMap = new HashMap<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "seq");
        PageRequest pageRequest = PageRequest.of(currentPage, 10, sort);
        //filter 만들기
        BoardSpecification specification = new BoardSpecification(searchType, searchText);
        Page<BoardEntity> pages = boardRepository.findAll(specification, pageRequest);

        PagingVo pageVO = new PagingVo();
        pageVO.setData(currentPage, (int)pages.getTotalElements());

        resultMap.put("pageHtml", pageVO.pageHtml());
        resultMap.put("currentPage", currentPage);
        resultMap.put("list", pages.getContent());
        resultMap.put("totalRows", pages.getTotalElements());

        return resultMap;
    }

    //게시글 개별 조회
    @Transactional
    public BoardDTO getBoardDetail(HttpServletRequest request, HttpServletResponse response, Integer seq) throws Exception {

        BoardEntity board = boardRepository.findById(seq).orElseThrow(() -> new Exception("없음"));

        if (!CookieUtils.checkCookie(request, seq.toString())){
            board.setReadCount(board.getReadCount() + 1);
            boardRepository.save(board);
            CookieUtils.updateCookie(request, response, seq.toString());
        }
        return BoardDTO.of(board);
    }

    public BoardDTO getBoardDetail(int seq) throws Exception {
        BoardEntity board = boardRepository.findById(seq).orElseThrow(() -> new Exception("없음"));

        return BoardDTO.of(board);
    }

    @Transactional
    public Map<String, Object> addBoard(BoardData.Request request, SecureUser user) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();

        //게시글 등록
        BoardEntity entity = new BoardEntity();
        entity.setTitle(request.getTitle());
        entity.setContents(request.getContents());
        entity.setWriter(user.getUsername());

        boardRepository.save(entity);

        List<MultipartFile> files = Arrays.asList(request.getFile());
        List<Map<String, Object>> fileMap
                = CommonFileUtils.uploadFiles(files, filePath, "board");

        if(!fileMap.isEmpty()){

            for(Map<String, Object> file : fileMap){

                BoardFileEntity fileEntity = new BoardFileEntity();
                fileEntity.setFileName(file.get("fileName").toString());
                fileEntity.setStoredFileName(file.get("storedFileName").toString());
                fileEntity.setFilePath(file.get("filePath").toString());
                fileEntity.setBoard(entity);
                boardFileRepository.save(fileEntity);

            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글 등록했습니다.");

        return resultMap;
    }

    //게시글 수정
    @Transactional
    public Map<String, Object> updateBoard(BoardData.Request request, SecureUser user) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        //기존 게시글 호출
        BoardEntity board = boardRepository.findById(request.getSeq()).orElseThrow(() -> new Exception("없음"));

        if (!user.isAuth() && !user.getUserName().equals(board.getWriter())) {
            throw new Exception("NOT Authorized");
        }

        board.setTitle(request.getTitle());
        board.setContents(request.getContents());
        board.setUpdateDate(LocalDateTime.now());

        //수정할 파일이 있으면 기존 파일 삭제 후 새로 등록
        if(!request.getFile().isEmpty()){
            //파일 삭제
            if (!board.getBoardFiles().isEmpty()) {
                deleteBoardFile(board.getBoardFiles());
            }

            List<MultipartFile> files = Arrays.asList(request.getFile());
            List<Map<String, Object>> fileMap
                    = CommonFileUtils.uploadFiles(files, filePath, "board");

            if(!fileMap.isEmpty()){
                //map to Object 로 변경하기 위한 객체 선언
                ObjectMapper objectMapper = new ObjectMapper();
                for(Map<String, Object> file : fileMap){
                    BoardFileEntity fileEntity = new BoardFileEntity();
                    fileEntity.setFileName(file.get("fileName").toString());
                    fileEntity.setStoredFileName(file.get("storedFileName").toString());
                    fileEntity.setFilePath(file.get("filePath").toString());
                    fileEntity.setBoard(board);
                    boardFileRepository.save(fileEntity);
                }
            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글 수정했습니다.");

        return resultMap;
    }

    @Transactional
    public Map<String, Object> deleteBoard(int seq, SecureUser user) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //기존 게시글 호출
        BoardEntity board = boardRepository.findById(seq).orElseThrow(() -> new Exception("없음"));

        if (!user.isAuth() && !user.getUserName().equals(board.getWriter())) {
            throw new Exception("NOT Authorized");
        }

        //파일 삭제
        if (!board.getBoardFiles().isEmpty()) {
            deleteBoardFile(board.getBoardFiles());
        }

        //게시글 삭제
        boardRepository.delete(board);

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글을 삭제했습니다.");
        return resultMap;
    }

    //파일삭제 - 게시글 수정, 게시글 삭제에 사용하는 공통 메서드
    private void deleteBoardFile(Set<BoardFileEntity> boardFiles) throws SQLException{
        //파일 삭제
        for(BoardFileEntity file : boardFiles){
            String path = file.getFilePath() + file.getFileName();
            CommonFileUtils.deleteFile(path);
            //DB 삭제
            boardFileRepository.delete(file);
        }
    }

}
