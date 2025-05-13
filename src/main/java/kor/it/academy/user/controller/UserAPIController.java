package kor.it.academy.user.controller;

import kor.it.academy.user.service.UserService;
import kor.it.academy.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    public ResponseEntity<Map<String , Object>> getUserData(@RequestParam(defaultValue = "0") int currentPage) {
        Map<String , Object> resultMap = new HashMap<>();

        try {
            resultMap.put("currentPage", currentPage);
            //결과 map 재사용
            resultMap = userService.getUserList(resultMap);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            e.printStackTrace();
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Map<String , Object>> deleteUser(String  userList) {
        Map<String , Object> resultMap = new HashMap<>();

        try {
            resultMap = userService.deleteUser(userList);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            e.printStackTrace();
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String,Object>> updateUser(@PathVariable(name = "userId") String userId,
                                                         @RequestBody User.Request userRequest) {
        Map<String,Object> resultMap = new HashMap<>();

        try {
            userRequest.setUserId(userId);
            resultMap = userService.updateUser(userRequest);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            e.printStackTrace();
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
