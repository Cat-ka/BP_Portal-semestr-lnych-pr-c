package com.portal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/subject")
public interface SubjectController {

    @PostMapping(path = "/add")
    public ResponseEntity<String> addSubject(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/update")
    public ResponseEntity<String> updateSubject(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteSubject(@PathVariable Integer id);

    @GetMapping(path = "/get")
    ResponseEntity<List<Map<String, Object>>> getSubjects(@RequestParam(required = false)
                                                          String filterValue);

    @GetMapping(path = "/get/{id}")
    ResponseEntity<Map<String, Object>> getSubjectById(@PathVariable Integer id);


}

