package kor.it.academy.user.controller;

import kor.it.academy.user.service.UserService;
import kor.it.academy.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserAPIController {

    private final UserService userService;

    //클라이언트에서 data를 json 객체로 넘겨줘야함
    @PostMapping("/")
    public ResponseEntity<Map<String , Object>> addUser(@RequestBody User.Request userRequest) {

        Map<String , Object> resultMap = new HashMap<>();

        try {
            resultMap = userService.addUser(userRequest);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            e.printStackTrace();
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
