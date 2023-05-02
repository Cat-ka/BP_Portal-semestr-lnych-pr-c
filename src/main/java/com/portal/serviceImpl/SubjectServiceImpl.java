package com.portal.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.portal.JWT.JwtFilter;
import com.portal.constents.PortalConstants;
import com.portal.model.Subject;
import com.portal.model.SubjectUser;
import com.portal.model.User;
import com.portal.repository.SubjectRepository;
import com.portal.repository.SubjectUserRepository;
import com.portal.repository.UserRepository;
import com.portal.service.SubjectService;
import com.portal.utils.PortalUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubjectUserRepository subjectUserRepository;


    @Override
    public ResponseEntity<String> addSubject(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                if (validateSubjectMap(requestMap)) {
                    String subjectName = requestMap.get("subjectName");
                    String teachers = requestMap.get("teachers");
                    List<Integer> teacherIds = Arrays.stream(teachers.split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());

                    Subject subject = new Subject();
                    subject.setName(subjectName);
                    subjectRepository.save(subject);

                    // Pridanie učiteľov k subjektu
                    for (Integer teacherId : teacherIds) {
                        User teacher = userRepository.findById(teacherId).orElse(null);
                        if (teacher != null && teacher.getRole().equals("teacher")) {
                            SubjectUser subjectUser = new SubjectUser();
                            subjectUser.setSubject(subject);
                            subjectUser.setUser(teacher);
                            subjectUserRepository.save(subjectUser);
                        } else {
                            return PortalUtils.getResponseEntity("Učiteľ s ID " + teacherId +
                                    " nebol nájdený.", HttpStatus.BAD_REQUEST);
                        }
                    }
                    return PortalUtils.getResponseEntity("Predmet bol úspešne pridaný.", HttpStatus.OK);
                } else {
                    return PortalUtils.getResponseEntity(PortalConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSubject(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                Integer subjectId = Integer.valueOf(requestMap.get("id"));
                String newName = requestMap.get("subjectName");
                List<Integer> userId = Arrays.stream(requestMap.getOrDefault("teachers", "").split(","))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                Optional<Subject> existingSubject = subjectRepository.findById(subjectId);
                if (existingSubject.isPresent()) {
                    //zmena mena predmetu
                    Subject updatedSubject = existingSubject.get();
                    updatedSubject.setName(newName);
                    subjectRepository.save(updatedSubject);

                    //získať zoznam aktuálnych užívateľov priradených k predmetu
                    List<SubjectUser> currentSubjectUsers = subjectUserRepository.findBySubjectId(subjectId);

                    //zmena aktuálne priradených úžívateľ
                    List<Integer> currentTeacherIds = currentSubjectUsers.stream()
                            .filter(subjectUser -> subjectUser.getUser().getRole().equals("teacher"))
                            .map(subjectUser -> subjectUser.getUser().getId())
                            .collect(Collectors.toList());

                    if (!userId.isEmpty()) {
                        // Find new teacher ids
                        List<Integer> newTeacherIds = userId.stream()
                                .filter(teacherId -> !currentTeacherIds.contains(teacherId))
                                .collect(Collectors.toList());

                        for (Integer teacherId : newTeacherIds) {
                            User teacher = userRepository.findById(teacherId).orElse(null);
                            if (teacher != null) {
                                SubjectUser subjectUser = new SubjectUser();
                                subjectUser.setSubject(updatedSubject);
                                subjectUser.setUser(teacher);
                                subjectUserRepository.save(subjectUser);
                            }
                        }

                        // Remove old teachers
                        currentSubjectUsers.stream()
                                .filter(subjectUser -> subjectUser.getUser().getRole().equals("teacher"))
                                .filter(subjectUser -> !userId.contains(subjectUser.getUser().getId()))
                                .forEach(subjectUserRepository::delete);
                    }


                    return PortalUtils.getResponseEntity("Predmet bol úspešne zmenený", HttpStatus.OK);
                } else {
                    return PortalUtils.getResponseEntity("Predmet s ID " + subjectId +
                            " nebol nájdený.", HttpStatus.BAD_REQUEST);
                }

            } else {
                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> deleteSubject(Integer id) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                if (subjectRepository.existsById(id)) {
                    subjectUserRepository.deleteSubjectUsersBySubjectId(id);
                    subjectRepository.deleteById(id);
                } else {
                    return PortalUtils.getResponseEntity("Predmet s ID " + id +
                            " nebol nájdený.", HttpStatus.BAD_REQUEST);
                }
                return PortalUtils.getResponseEntity("Predmet bol úspešne odstránený.", HttpStatus.OK);
            } else {
                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

   /* @Override
    public ResponseEntity<List<Map<String, Object>>> getSubjects(String filterValue) {
        List<Map<String, Object>> subjects = new ArrayList<>();
        //if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
            log.info("Inside if getAllCategory");

            List<Subject> subjectList = subjectRepository.findAll();
            // for each subject, get its name and list of teachers
            for (Subject subject : subjectList) {
                Map<String, Object> subjectInfo = new HashMap<>();
                subjectInfo.put("subjectNames", subject.getName());
                subjectInfo.put("id", subject.getId());

                List<SubjectUser> subjectUsers = subjectUserRepository.findBySubjectId(subject.getId());
                List<String> teacherNames = subjectUsers.stream()
                        .filter(subjectUser -> subjectUser.getUser().getRole().equals("teacher"))
                        .map(subjectUser -> subjectUser.getUser().getName())
                        .collect(Collectors.toList());

                subjectInfo.put("teacherNames", teacherNames);

                subjects.add(subjectInfo);
            }
        //}
            return new ResponseEntity<>(subjects, HttpStatus.OK);
    }*/

    @Override
    public ResponseEntity<List<Map<String, Object>>> getSubjects(String filterValue) {
        List<Map<String, Object>> subjects = new ArrayList<>();
        /*String currentUserName = jwtFilter.getCurrentUser();
        User currentUser = userRepository.findByEmail(currentUserName);
        Integer currentUserId = currentUser.getId();*/

        //List<SubjectUser> subjectIds = new ArrayList<>();
        List<Subject> subjectList;
        if (filterValue != null && !filterValue.isEmpty()) {
            subjectList = subjectUserRepository.findByUserId(Integer.parseInt(filterValue));
        } else {
            subjectList = subjectRepository.findAll();
        }

        log.info("Inside if getAllCategory");

        //List<Subject> subjectList = subjectRepository.findAll();
        // for each subject, get its name and list of teachers
        for (Subject subject : subjectList) {
            Map<String, Object> subjectInfo = new HashMap<>();
            subjectInfo.put("subjectNames", subject.getName());
            subjectInfo.put("id", subject.getId());

            List<SubjectUser> subjectUsers = subjectUserRepository.findBySubjectId(subject.getId());
            List<String> teacherNames = subjectUsers.stream()
                    .filter(subjectUser -> subjectUser.getUser().getRole().equals("teacher"))
                    .map(subjectUser -> subjectUser.getUser().getName())
                    .collect(Collectors.toList());

            subjectInfo.put("teacherNames", teacherNames);

            subjects.add(subjectInfo);

        }
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Map<String, Object>> getSubjectById(@RequestParam Integer id) {
        log.info("Inside if getSubjectById");
        Optional<Subject> subjectData = subjectRepository.findById(id);

        if (subjectData.isPresent()) {
            Subject subject = subjectData.get();

            Map<String, Object> subjectInfo = new HashMap<>();
            subjectInfo.put("subjectName", subject.getName());
            subjectInfo.put("id", subject.getId());

            List<SubjectUser> subjectUsers = subjectUserRepository.findBySubjectId(subject.getId());
            List<String> teacherNames = subjectUsers.stream()
                    .filter(subjectUser -> subjectUser.getUser().getRole().equals("teacher"))
                    .map(subjectUser -> subjectUser.getUser().getName())
                    .collect(Collectors.toList());

            subjectInfo.put("teacherNames", teacherNames);
            return new ResponseEntity<>(subjectInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private boolean validateSubjectMap(Map<String, String> requestMap) {
        return requestMap.containsKey("subjectName") && requestMap.containsKey("teachers");
    }

}
