package com.portal.service;

import com.portal.model.User;
import com.portal.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<List<String>> getAllTeachers();

    ResponseEntity<List<UserWrapper>> getAll();

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<String> updateRole(Map<String, String> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllTechersWrap();

    ResponseEntity<String> getNameCurrentUser();

    ResponseEntity<UserWrapper> getCurrentUserInfo();
}
