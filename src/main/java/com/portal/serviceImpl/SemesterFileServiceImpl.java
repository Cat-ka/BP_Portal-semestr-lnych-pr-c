package com.portal.serviceImpl;

import com.portal.constents.PortalConstants;
import com.portal.model.SemesterFile;
import com.portal.model.SemesterProject;
import com.portal.repository.SemesterFileRepository;
import com.portal.repository.SemesterProjectRepository;
import com.portal.service.SemesterFileService;
import com.portal.utils.PortalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SemesterFileServiceImpl implements SemesterFileService {

    @Autowired
    SemesterFileRepository semesterFileRepository;

    @Autowired
    SemesterProjectRepository semesterProjectRepository;


    @Override
    public ResponseEntity<String> uploadFile(Integer semesterProjectId, byte[] fileData) {
        try {
            Optional<SemesterProject> semesterProject = semesterProjectRepository.findById(semesterProjectId);
            if (semesterProject.isPresent()) {
                SemesterProject project = semesterProject.get();
                SemesterFile semesterFile = new SemesterFile();
                semesterFile.setFileData(fileData);
                semesterFile.setFileName("fileName");
                semesterFile.setSemesterProject(project);
                semesterFileRepository.save(semesterFile);
                return PortalUtils.getResponseEntity("Súbor k semestrálnej práci č. "
                        + semesterProjectId + " bol úspešne nahratý.", HttpStatus.NOT_FOUND);
            } else {
                return PortalUtils.getResponseEntity("Semestrálna práca ID " + semesterProjectId + " nebola nájdená.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(Integer id) {
        try {
            SemesterFile semesterFile = semesterFileRepository.findById(id)
                    .orElseThrow(() -> new Exception("File not found with id " + id));

            // Vytvorí sa Resource z bytov súboru
            ByteArrayResource resource = new ByteArrayResource(semesterFile.getFileData());

            // Nastavia sa hlavičky pre odpoveď
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + semesterFile.getFileName());
            //headers.add(HttpHeaders.CONTENT_TYPE, semesterFile.getFileType());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(semesterFile.getFileData().length));

            // Vráti sa ResponseEntity so súborom ako Resource a hlavičkami
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



}

