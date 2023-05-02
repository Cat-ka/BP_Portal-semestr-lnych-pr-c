package com.portal.repository;

import com.portal.model.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Transactional
    void deleteById(Integer Id);


}
