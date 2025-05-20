package kor.it.academy.home;

import kor.it.academy.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class HomeController {

    //  (생성자를 final로 => not null 같은 개념)
    private final BoardService boardService;

    /*의존성 주입을 위한 annotation
        @AutoWired
        @Inject
        @Resource(이름)
        @Qualifier(Bean 이름)
    */
    // 1.
//    @Autowired
//    private BoardService boardService;

    /*
        2.
        public void setBoardService(BoardService boardService){
            this.boardService = boardService;
        }
    */

//    //일꾼 메서드
//    @RequestMapping(value = "/home", method = RequestMethod.GET)
//    public String home(Model model) {
//        model.addAttribute("msg","안녕");
//        return "home";
//    }

    //일꾼 메서드
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView view = new ModelAndView();
        view.setViewName("home");
        view.addObject("msg", "안녕2");
        return view;
    }
}
