package kor.it.academy.board.service;

import kor.it.academy.board.entity.BoardEntity;
import kor.it.academy.board.mapper.BoardRepository;
import kor.it.academy.board.mapper.specification.BoardSpecification;
import kor.it.academy.board.vo.BoardDTO;
import kor.it.academy.common.vo.PagingVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardJPAService {

    private final BoardRepository boardRepository;

    public Map<String , Object> searchBoardList(int currentPage, String searchType, String searchText) {
        Map<String , Object> resultMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
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
}
