package com.portal.serviceImpl;

import com.portal.JWT.JwtFilter;
import com.portal.constents.PortalConstants;
import com.portal.model.SemesterProject;
import com.portal.model.Subject;
import com.portal.model.User;
import com.portal.repository.SemesterProjectRepository;
import com.portal.repository.SubjectRepository;
import com.portal.repository.UserRepository;
import com.portal.service.SemesterProjectService;
import com.portal.utils.PortalUtils;
import com.portal.wrapper.SemesterProjectWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class SemesterProjectServiceImpl implements SemesterProjectService {

    private static final Logger logger = LoggerFactory.getLogger(SemesterProjectServiceImpl.class);

    // ...

    @Autowired
    UserRepository userRepository;

    @Autowired
    SemesterProjectRepository semesterProjectRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserDetailServiceImpl userDetailServiceImpl;

    @Override
    public ResponseEntity<String> addSemesterProject(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                if (validateSemesterMap(requestMap)) {
                    Integer subjectId = Integer.parseInt(requestMap.get("subjectId"));
                    Optional<Subject> subject = subjectRepository.findById(subjectId);
                    if (subject.isEmpty()) {
                        return PortalUtils.getResponseEntity("Predmet nebol nájdený. Zmeňte dáta.", HttpStatus.NOT_FOUND);
                    } else {
                        SemesterProject project = new SemesterProject();
                        project = getSemesterFromMap(requestMap);
                        semesterProjectRepository.save(project);
                        return PortalUtils.getResponseEntity("Semestrálna práca bola úspešne pridaná.", HttpStatus.OK);
                    }
                } else {
                    return PortalUtils.getResponseEntity("Nie sú vyplnené všetky potrebné dáta.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return PortalUtils.getResponseEntity("Nie ste autoriovaný pre zadávanie semestrálnych prác.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getAllSemesterProjects() {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                return new ResponseEntity<>(semesterProjectRepository.getAllProjects(), HttpStatus.OK);
                //ak si admin alebo učitel, tak mi vrát zoznam semestrálnych prác
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
                //ak nie si admin, tak ti vrátim status, že nemáš oprávnenie a takúto operáciu
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteSemesterProject(Integer id) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                if (semesterProjectRepository.existsById(id)) {
                    semesterProjectRepository.deleteById(id);
                } else {
                    return PortalUtils.getResponseEntity("Semestrálna práca s ID " + id +
                            " nebola nájdená.", HttpStatus.BAD_REQUEST);
                }
                return PortalUtils.getResponseEntity("Semestrálna práca bola úspešne odstránená.",
                        HttpStatus.OK);
            } else {
                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                Optional<SemesterProject> optional = semesterProjectRepository.findById(Integer.valueOf(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    semesterProjectRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    //parsujeme to na intiger, inak by to vyhodilo chybu
                    return PortalUtils.getResponseEntity("Status semestrálnej práce bol úspešne zmenený.", HttpStatus.OK);
                    //musíš byť admin a status true, aby si niekomu inému mohol meniť status
                } else {
                    PortalUtils.getResponseEntity("ID používateľa nebolo nájdené.", HttpStatus.NOT_FOUND);
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
    public ResponseEntity<List<SemesterProjectWrapper>> getPublishedProjects() {
        try {
            return new ResponseEntity<>(semesterProjectRepository.getPublishedProjects(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> assignProject(Map<String, String> requestMap) {
        try {
            Integer semesterProjectId = Integer.valueOf(requestMap.get("id"));
            Optional<SemesterProject> semesterProjectOptional = this.semesterProjectRepository.findById(semesterProjectId);

            if (semesterProjectOptional.isPresent()) {
                SemesterProject semesterProject = semesterProjectOptional.get();

                if (semesterProject.isAvailable() && semesterProject.getUser() == null) {
                    String email = jwtFilter.getCurrentUser();
                    User user = userRepository.findByEmail(email);
                    Integer userId = user.getId();

                    this.semesterProjectRepository.addUserId(userId, semesterProjectId);
                    this.semesterProjectRepository.updateAvailable(false, semesterProjectId);
                    return PortalUtils.getResponseEntity("Používateľ bol úspešne priradený.", HttpStatus.OK);

                } else {
                    return PortalUtils.getResponseEntity("Tento projekt už je priradený alebo nie je k dispozícii.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return PortalUtils.getResponseEntity("Semestrálna práca s týmto ID nebola nájdená.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cancelProject(Map<String, String> requestMap) {
        try {
            Integer semesterProjectId = Integer.parseInt(requestMap.get("id"));
            Optional<SemesterProject> semesterProjectOptional = this.semesterProjectRepository.findById(semesterProjectId);
            if (semesterProjectOptional.isPresent()) {
                SemesterProject semesterProject = semesterProjectOptional.get();
                if (!semesterProject.isAvailable() && semesterProject.getUser() != null) {
                    String email = jwtFilter.getCurrentUser();
                    User user = userRepository.findByEmail(email);
                    Integer userId = user.getId();
                    if (userId.equals(semesterProject.getUser().getId())) {
                        this.semesterProjectRepository.removeUserId(semesterProjectId);
                        this.semesterProjectRepository.updateAvailable(true, semesterProjectId);
                        return PortalUtils.getResponseEntity("Používateľ bol úspešne odstránený z projektu.", HttpStatus.OK);
                    } else {
                        return PortalUtils.getResponseEntity("Používateľ nie je priradený k tomuto projektu.", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return PortalUtils.getResponseEntity("Tento projekt nie je priradený žiadnemu používateľovi alebo je už dostupný.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return PortalUtils.getResponseEntity("Semestrálna práca s týmto ID nebola nájdená.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity("Chyba v metode cancelProject", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<SemesterProjectWrapper> getPublishedProjectById(Integer id) {
        try {
            Optional<SemesterProject> projectOptional = semesterProjectRepository.findById(id);
            if (projectOptional.isPresent()) {
                SemesterProject project = projectOptional.get();
                SemesterProjectWrapper wrapper = this.getWrapper(project);
                return new ResponseEntity<>(wrapper, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new SemesterProjectWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new SemesterProjectWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*@Override
    public ResponseEntity<ByteArrayResource> downloadFile(Integer semesterProjectId) {
        Optional<Blob> fileDataOptional = getFileData(semesterProjectId);

        if (fileDataOptional.isPresent()) {
            Blob fileData = fileDataOptional.get();
            ByteArrayResource resource = new ByteArrayResource(fileData);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "your_desired_filename" + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }*/

    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(Integer semesterProjectId) {
        Optional<SemesterProject> semesterProjectOptional = semesterProjectRepository.findById(semesterProjectId);
        if (semesterProjectOptional.isPresent()) {
            SemesterProject semesterProject = semesterProjectOptional.get();
            try {
                Blob fileData = semesterProject.getFileData();
                byte[] fileBytes = fileData.getBytes(1, (int) fileData.length());
                ByteArrayResource resource = new ByteArrayResource(fileBytes);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(ContentDisposition.attachment().filename(semesterProject.getFileName()).build());
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }


    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getSemestersForTeacher(Integer id) {
        try {
            if (userDetailServiceImpl.getUserDetail().getRole().equals("teacher")) {
                List<SemesterProjectWrapper> semesters = semesterProjectRepository.getSemestersForTeacher(id);
                if (semesters != null && !semesters.isEmpty()) {
                    return new ResponseEntity<List<SemesterProjectWrapper>>(semesters, HttpStatus.OK);
                } else {
                    return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.NO_CONTENT);
                }
            } else {
                PortalUtils.getResponseEntity("Nemáte prístup k týmto dátam.", HttpStatus.UNAUTHORIZED);
                return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getSemestersByCurrentUser() {
        try {
            if (userDetailServiceImpl.getUserDetail().getRole().equals("teacher")) {
                Integer userId = getCurrentUserId();
                List<SemesterProjectWrapper> semesters = semesterProjectRepository.getSemestersByCurrentUser(userId);
                if (semesters != null && !semesters.isEmpty()) {
                    return new ResponseEntity<List<SemesterProjectWrapper>>(semesters, HttpStatus.OK);
                } else {
                    return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getSemesterProjectsForStudent() {
        try {
            Integer userId = getCurrentUserId();
            List<SemesterProjectWrapper> semesters = semesterProjectRepository.getSemesterProjectsForStudent(userId);

            if (semesters != null && !semesters.isEmpty()) {
                return new ResponseEntity<List<SemesterProjectWrapper>>(semesters, HttpStatus.OK);
            } else {
                return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<List<SemesterProjectWrapper>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Blob> getFileData(Integer semesterProjectId) {
        Optional<SemesterProject> semesterProjectOptional = semesterProjectRepository.findById(semesterProjectId);
        if (semesterProjectOptional.isPresent()) {
            SemesterProject semesterProject = semesterProjectOptional.get();
            return Optional.of(semesterProject.getFileData());
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> getCurrentUser() {
        String email = jwtFilter.getCurrentUser();
        User user = userRepository.findByEmail(email);
        Integer userId = user.getId();
        String name = user.getFullName();
        return PortalUtils.getResponseEntity(String.valueOf(userId), HttpStatus.OK);
    }

    public Integer getCurrentUserId() {
        String email = jwtFilter.getCurrentUser();
        User user = userRepository.findByEmail(email);
        Integer userId = user.getId();
        return userId;
    }


    @Override
    public ResponseEntity<String> uploadFile(Integer semesterProjectId, MultipartFile fileData) {
        //Integer semesterProjectId = Integer.valueOf(requestMap.get("id"));
        //String base64File = requestMap.get("file");
        if (fileData.isEmpty()) {
            return PortalUtils.getResponseEntity("Súbor je prázdny.", HttpStatus.BAD_REQUEST);
        }
        try {
            String fileName = fileData.getOriginalFilename();
            Optional<SemesterProject> semesterProjectOptional = this.semesterProjectRepository.findById(semesterProjectId);
            if (semesterProjectOptional.isPresent()) {
                SemesterProject semesterProject = semesterProjectOptional.get();
                Blob fileBlob = new SerialBlob(fileData.getBytes());
                semesterProject.setFileData(fileBlob);
                semesterProject.setFileName(fileName);
                this.semesterProjectRepository.save(semesterProject);
                return PortalUtils.getResponseEntity("Súbor bol úspešne nahraný.", HttpStatus.OK);
            } else {
                return PortalUtils.getResponseEntity("Projekt s týmto ID sa nenašiel.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSemesterMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && (requestMap.get("name") != null) && requestMap.containsKey("description")
                && !(requestMap.get("description").equals(null)) && !(requestMap.get("term").equals("1970-01-01"))
                && requestMap.containsKey("term") && requestMap.containsKey("subjectId")) {
            return true;
        }
        return false;
    }

    private SemesterProject getSemesterFromMap(Map<String, String> requestMap) {
        User user = userRepository.findByEmail(jwtFilter.getCurrentUser());
        SemesterProject semesterProject = new SemesterProject();
        semesterProject.setName(requestMap.get("name"));
        semesterProject.setDescription(requestMap.get("description"));
        semesterProject.setCreatedById(user.getId());
        semesterProject.setCreatedBy(user.getFirstName() + " " + user.getLastName()); // pridanie mena a priezviska prihláseného používateľa
        semesterProject.setStatus(requestMap.get("status"));
        semesterProject.setAvailable(true);
        semesterProject.setTerm(LocalDate.parse(requestMap.get("term")));
        Integer subjectId = Integer.parseInt(requestMap.get("subjectId"));
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        semesterProject.setSubject(subject);
        return semesterProject;
    }

    private SemesterProjectWrapper getWrapper(SemesterProject project) {
        SemesterProjectWrapper wrapper = new SemesterProjectWrapper(project.getId(), project.getName(),
                project.getDescription(), project.getTerm(), project.getCreatedById(), project.getCreatedBy(),
                project.getAvailable(), project.getSubject().getId(), project.getSubject().getName(),
                project.getUser() != null ? project.getUser().getId() : null,
                project.getUser() != null ? project.getUser().getFirstName() : null,
                project.getUser() != null ? project.getUser().getLastName() : null,
                project.getUser() != null ? project.getUser().getFullName() : null);
        return wrapper;
    }


}
