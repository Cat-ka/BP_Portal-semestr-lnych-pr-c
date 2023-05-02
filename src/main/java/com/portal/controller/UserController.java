package com.portal.controller;

import com.portal.model.User;
import com.portal.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserController {

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<UserWrapper>> getAllUsers();

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<UserWrapper>> getAll();

    @GetMapping(path = "/getTeachers")
    public ResponseEntity<List<String>> getAllTeachers();

    @GetMapping(path="/getTeachersWrap")
    public ResponseEntity<List<UserWrapper>> getAllTeachersWrap();

    @PostMapping(path = "/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/updateRole")
    public ResponseEntity<String> updateRole(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);
    //do postmana som vkladala len staré a nové heslo, užívateľov mail si našiel sám

    @PostMapping(path = "/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/profile")
    public ResponseEntity<String> getCurrentUser();
}
