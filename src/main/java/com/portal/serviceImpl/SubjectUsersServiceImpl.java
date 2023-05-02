package com.portal.serviceImpl;

import com.portal.JWT.JwtFilter;
import com.portal.constents.PortalConstants;
import com.portal.model.Subject;
import com.portal.repository.SubjectRepository;
import com.portal.repository.SubjectUserRepository;
import com.portal.repository.UserRepository;
import com.portal.service.SubjectUserService;
import com.portal.utils.PortalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubjectUsersServiceImpl implements SubjectUserService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    SubjectUserRepository subjectUserRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<String> deleteSubjectUsersBySubjectId(Integer subjectId) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                // ešte tu môžem dorobiť overenie či dané id ktoré idem mazať tak existuje
                subjectUserRepository.deleteBySubjectId(subjectId);
            } else {
                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSubjectUsers(Map<String,String> requestMap) {
        return null;
    }

    @Override
    public ResponseEntity<List<String>> getSubjectByUserId(Integer userId) {
        List<Subject> subjects = subjectUserRepository.findByUserId(userId);
        List<String> subjectNames = subjects.stream()
                .map(Subject::getName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subjectNames);
    }



//        try {
//            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
//                // ešte tu môžem dorobiť overenie či dané id ktoré idem mazať tak existuje
//                //Integer subjectId = Integer.parseInt(requestMap.get("subjectId"));
//                if (subjectId != null) {
//                    List<Integer> teachers = Arrays.asList(requestMap.get("teacherIds").split(","))
//                            .stream()
//                            .map(s -> Integer.parseInt(s.trim()))
//                            .collect(Collectors.toList());
//
//                    List<SubjectUser> subjectUsers = subjectUserRepository.findBySubjectId(subjectId);
//                    subjectUserRepository.deleteAll(subjectUsers);
//
//                    for (Integer teacherId : teacherIds) {
//                        SubjectUser subjectUser = new SubjectUser();
//                        subjectUser.setSubject(subjectRepository.findById(subjectId).orElse(null));
//                        subjectUser.setUser(userRepository.findById(teacherId).orElse(null));
//                        subjectUserRepository.save(subjectUser);
//                    }
//                } else {
//                    return PortalUtils.getResponseEntity("Subject id does not exist.", HttpStatus.OK);
//                }
//            } else {
//                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//
//    }


}
