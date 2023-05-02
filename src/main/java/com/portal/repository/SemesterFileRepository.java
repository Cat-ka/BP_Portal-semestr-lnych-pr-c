package com.portal.repository;

import com.portal.model.SemesterFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterFileRepository extends JpaRepository<SemesterFile, Integer> {

}
