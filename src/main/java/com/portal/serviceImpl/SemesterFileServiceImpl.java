package com.portal.serviceImpl;

import com.portal.constents.PortalConstants;
import com.portal.model.SemesterFile;
import com.portal.model.SemesterProject;
import com.portal.repository.SemesterFileRepository;
import com.portal.repository.SemesterProjectRepository;
import com.portal.service.SemesterFileService;
import com.portal.utils.PortalUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class SemesterFileServiceImpl implements SemesterFileService {

    @Autowired
    SemesterFileRepository semesterFileRepository;

    @Autowired
    SemesterProjectRepository semesterProjectRepository;


//    @Override
//    public ResponseEntity<String> uploadFile(Integer semesterProjectId, byte[] fileData) {
//        try {
//            Optional<SemesterProject> semesterProject = semesterProjectRepository.findById(semesterProjectId);
//            if (semesterProject.isPresent()) {
//                SemesterProject project = semesterProject.get();
//                SemesterFile semesterFile = new SemesterFile();
//                semesterFile.setFileData(fileData);
//                semesterFile.setFileName("fileName");
//                semesterFile.setSemesterProject(project);
//                semesterFileRepository.save(semesterFile);
//                return PortalUtils.getResponseEntity("Súbor k semestrálnej práci č. "
//                        + semesterProjectId + " bol úspešne nahratý.", HttpStatus.NOT_FOUND);
//            } else {
//                return PortalUtils.getResponseEntity("Semestrálna práca ID " + semesterProjectId + " nebola nájdená.", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @Override
//    public ResponseEntity<ByteArrayResource> downloadFile(Integer id) {
//        try {
//            SemesterFile semesterFile = semesterFileRepository.findById(id)
//                    .orElseThrow(() -> new Exception("File not found with id " + id));
//
//            // Vytvorí sa Resource z bytov súboru
//            ByteArrayResource resource = new ByteArrayResource(semesterFile.getFileData());
//
//            // Nastavia sa hlavičky pre odpoveď
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + semesterFile.getFileName());
//            //headers.add(HttpHeaders.CONTENT_TYPE, semesterFile.getFileType());
//            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(semesterFile.getFileData().length));
//
//            // Vráti sa ResponseEntity so súborom ako Resource a hlavičkami
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(resource);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }

    @Transactional
    @Override
    public ResponseEntity<String> uploadFile(Integer projectId, MultipartFile file) {
        try {
            Optional<SemesterProject> semesterProjectOptional = semesterProjectRepository.findById(projectId);
            if (semesterProjectOptional.isPresent()) {
                //SemesterProject project = semesterProjectOptional.get();
                SemesterFile semesterFile = new SemesterFile();
                semesterFile.setFileData(file.getBytes());
                semesterFile.setFileName(file.getOriginalFilename());
                semesterFile.setType(file.getContentType());
                semesterFile.setSemesterProject(semesterProjectOptional.get());

                //semesterFile.setSemesterProject(project);
                semesterFileRepository.save(semesterFile);
                return PortalUtils.getResponseEntity("Súbor k semestrálnej práci č. "
                        + projectId + " bol úspešne nahratý.", HttpStatus.OK);
            } else {
                return PortalUtils.getResponseEntity("Semestrálna práca ID " + projectId + " nebola nájdená.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Integer id) {
        try {
            Optional<SemesterFile> semesterFileOptional = semesterFileRepository.findById(id);
            if(semesterFileOptional.isPresent()) {

                SemesterFile semesterFile = semesterFileOptional.get();
                ByteArrayResource resource = new ByteArrayResource(semesterFile.getFileData());
                //String fileType = semesterFile.getType().split("/")[1];
                //String fileName = semesterFile.getFileName() + "." + fileType;
                String fileName = semesterFile.getFileName();
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                        .contentType(MediaType.parseMediaType(semesterFile.getType()))
                        .body(resource);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.parseMediaType(semesterFile.getType()));
//                headers.setContentDisposition(ContentDisposition.builder("attachment").filename(semesterFile.getFileName()).build());

                //semesterFileOptional.get().getFileData();
                //return new ResponseEntity<>(HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<List<SemesterFile>> getFilesByProjectId(Integer projectId) {
        try {
            List<SemesterFile> files = semesterFileRepository.findBySemesterProjectId(projectId);
            if (files != null) {
                return new ResponseEntity<>(files, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

