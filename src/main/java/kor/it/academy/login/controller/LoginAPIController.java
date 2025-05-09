package kor.it.academy.login.controller;

import jakarta.servlet.http.HttpSession;
import kor.it.academy.login.service.LoginService;
import kor.it.academy.login.vo.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginAPIController {

    private final LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<Map<String , Object>> login(LoginUser.LoginRequest loginRequest,
                                                      HttpSession session) {
        Map<String , Object> response = new HashMap<>();
        try{
            response = loginService.login(loginRequest, session);
        } catch (Exception e) {
            response.put("resultCode", 500);
            e.printStackTrace();
        }

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
