package kor.it.academy.user.controller;

import kor.it.academy.user.service.UserService;
import kor.it.academy.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public ModelAndView join() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/user/joinForm");
        try {
            List<User.UserAuth> authList = userService.getUserAuthList();
            view.addObject("authList", authList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
