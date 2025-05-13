package kor.it.academy.user.controller;

import kor.it.academy.user.service.UserService;
import kor.it.academy.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/list")
    public ModelAndView getUserList(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage) {
        ModelAndView view = new ModelAndView();

        view.addObject("currentPage", currentPage);
        view.setViewName("views/user/userList");
        return view;
    }

    @GetMapping("/detail")
    public ModelAndView getUserDetail(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage,
                                      @RequestParam String userId) {
        ModelAndView view = new ModelAndView();
        try {
            User.Response user = userService.getUser(userId);

            view.addObject("currentPage", currentPage);
            view.addObject("userId", userId);
            view.addObject("user", user);
        }catch (Exception e) {
            e.printStackTrace();
        }
        view.setViewName("views/user/detail");
        return view;
    }

    @GetMapping("/detail/view")
    public ModelAndView userUpdateView(@RequestParam(name = "currentPage", defaultValue = "0")int currentPage,
                                      @RequestParam String userId) {
        ModelAndView view = new ModelAndView();
        try {
            User.Response user = userService.getUser(userId);
            List<User.UserAuth> authList = userService.getUserAuthList();

            view.addObject("authList", authList);
            view.addObject("currentPage", currentPage);
            view.addObject("userId", userId);
            view.addObject("user", user);

            String agency = user.getMobileNumber().substring(0, 3);
            String number = user.getMobileNumber().substring(3);

            view.addObject("agency", agency);
            view.addObject("userMobile", number);

        }catch (Exception e) {
            e.printStackTrace();
        }
        view.setViewName("views/user/updateForm");
        return view;
    }
}
