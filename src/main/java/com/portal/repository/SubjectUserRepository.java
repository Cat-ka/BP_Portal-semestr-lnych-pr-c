package com.portal.repository;

import com.portal.model.Subject;
import com.portal.model.SubjectUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SubjectUserRepository extends JpaRepository<SubjectUser, Integer> {

   @Transactional
   void deleteBySubjectId(Integer subjectId);

   List<SubjectUser> findBySubjectId(Integer subjectId);

   @Transactional
   void deleteSubjectUsersBySubjectId(Integer subjectId);

   @Query("SELECT s FROM Subject s JOIN s.subjectUsers su WHERE su.user.id = :userId")
   List<Subject> findByUserId(@Param("userId") Integer userId);


}
