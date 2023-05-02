package com.portal.serviceImpl;

import com.portal.repository.SemesterProjectRepository;
import com.portal.repository.SubjectRepository;
import com.portal.repository.UserRepository;
import com.portal.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SemesterProjectRepository semesterProjectRepository;

    //čo chcem počítať, to sem supnem
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("subject", subjectRepository.count());
        map.put("user", userRepository.count());
        map.put("teacher", userRepository.countByRole("teacher"));
        map.put("semesterProject", semesterProjectRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
