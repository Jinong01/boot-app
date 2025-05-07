package kor.it.academy.board.controller;


import kor.it.academy.board.service.BoardService;
import kor.it.academy.board.vo.BoardData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final RequestToViewNameTranslator viewNameTranslator;

//    @RequestMapping(value = "/board/list", method = RequestMethod.GET)
//    public ModelAndView getBoardList() {
//        ModelAndView view = new ModelAndView();
//        view.setViewName("views/board/boardList");
//        return view;
//    }

    @GetMapping("/list")
    public ModelAndView getBoardList(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage) {
        ModelAndView view = new ModelAndView();
        BoardData.ResultData resultData = null;
        try{
            resultData = boardService.getBoardList(currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.addObject("data", resultData);
        view.setViewName("views/board/boardList");
        return view;
    }

    @GetMapping(value = "/form/view")
    public ModelAndView getBoardForm(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage) {
        ModelAndView view = new ModelAndView();

        view.addObject("currentPage", currentPage);
        view.setViewName("views/board/boardWrite");
        return view;
    }

    @GetMapping(value = "/detail/view")
    public ModelAndView getBoardDetail(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage,
                                     @RequestParam int seq) {
        ModelAndView view = new ModelAndView();
        BoardData.Detail detail = null;

        try{
            detail = boardService.getBoardDetail(seq);
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.addObject("currentPage", currentPage);
        view.addObject("board", detail);
        view.setViewName("views/board/boardDetail");
        return view;
    }

    @GetMapping(value = "/update/view")
    public ModelAndView getBoardUpdateView(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage,
                                       @RequestParam int seq) {
        ModelAndView view = new ModelAndView();
        BoardData.Detail detail = null;

        try{
            detail = boardService.getBoardDetail(seq);
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.addObject("currentPage", currentPage);
        view.addObject("board", detail);
        view.addObject("isFile", detail.getFiles().size() > 0 ? "true" : "false");
        view.setViewName("views/board/boardUpdate");
        return view;
    }

    /*
        ModelAttribute 는
        1. 클라이언트에서 서버로 넘어오는 데이터들을 객체 타입으로 전달 받기 위해 사용
        2. view 리턴할 때 model(데이터 객체)로 사용이 가능하다.
        3. 객체가 지닌 멤버변수의 이름은 클라이언트에서 전송되는 데이터의 key 값과 동일해야한다.
        4. annotation 은 생략이 가능하다
    */
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addBoard(@ModelAttribute BoardData.Request request) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = boardService.addBoard(request);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            resultMap.put("resultMsg", "게시글 등록 실패했습니다.");
            e.printStackTrace();
        }
        return resultMap;
    }
}
