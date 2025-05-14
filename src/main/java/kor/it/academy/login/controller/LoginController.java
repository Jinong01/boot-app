package kor.it.academy.login.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/form")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/login/form");
        return view;
    }

    @GetMapping("/out")
    public ModelAndView logOut(HttpSession session) {

        //세션에서 사용자 정보 지우기
        if (session.getAttribute("userInfo") != null) {
            session.removeAttribute("userInfo");
        }

        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/login/form");
        return view;
    }

    @GetMapping("/error")
    public ModelAndView error() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/login/error");
        return view;
    }
}
