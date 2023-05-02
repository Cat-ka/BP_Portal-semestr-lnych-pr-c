package com.portal.controller;


import com.portal.model.Subject;
import com.portal.wrapper.UserWrapper;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/subjectUser")
public interface SubjectUserController {


    @GetMapping(path = "/get")
    public ResponseEntity<List<String>>findByUserId(Integer userId);

}
