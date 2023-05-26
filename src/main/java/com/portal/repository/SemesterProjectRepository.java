package com.portal.repository;

import com.portal.model.SemesterProject;
import com.portal.wrapper.SemesterProjectWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterProjectRepository extends JpaRepository<SemesterProject, Integer> {

    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper (s.id, s.name, s.description, " +
            "s.term, s.status, s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
            "CONCAT(s.user.firstName, ' ', s.user.lastName)) AS fullName FROM SemesterProject s LEFT JOIN s.user")
    List<SemesterProjectWrapper> getAllProjects();

    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper (s.id, s.name, s.description, " +
            "s.term, s.status, s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
            "CONCAT(s.user.firstName, ' ', s.user.lastName)) AS fullName FROM SemesterProject s LEFT JOIN s.user where " +
            "s.status = 'true'")
    List<SemesterProjectWrapper> getPublishedProjects();

    //Vyberie všetky semestrálne práce za predmety, ktoré učí daný učiteľ
    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper (s.id, s.name, s.description, " +
            "s.term, s.status, s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
            "CONCAT(s.user.firstName, ' ', s.user.lastName)) AS fullName FROM SemesterProject s LEFT JOIN s.user u INNER JOIN " +
            "s.subject.subjectUsers su WHERE su.user.id = :userId")
    List<SemesterProjectWrapper> getSemestersForTeacher(@Param("userId") Integer id);

    //Vyberie všetky semestrálne práce, ktoré vytvoril daný učiteľ.
    //tu by bolo dobré dať podmienku že createdById = currentUserId
    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper (s.id, s.name, s.description, " +
            "s.term, s.status, s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
            "CONCAT(s.user.firstName, ' ', s.user.lastName)), s.fileName AS fullName, s.fileName  FROM SemesterProject s LEFT JOIN s.user u " +
            "WHERE s.createdById = :currentUserId")
    List<SemesterProjectWrapper> getSemestersByCurrentUser(@Param("currentUserId") Integer userId);

    //pridané fileName a fileData
//    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper (s.id, s.name, s.description, " +
//            "s.term, s.status, s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
//            "CONCAT(s.user.firstName, ' ', s.user.lastName), f.fileName, f.fileData) " +
//            "FROM SemesterProject s LEFT JOIN s.user u LEFT JOIN s.files f " +
//            "WHERE s.createdById = :currentUserId")
//    List<SemesterProjectWrapper> getSemestersByCurrentUser(@Param("currentUserId") Integer userId);


    //Vyberie všetky semestrálne práce na ktoré je prihlásený momentálne prihlásený používateľ
    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper(s.id, s.name, s.description, s.term, s.status, " +
            "s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
            "CONCAT(s.user.firstName, ' ', s.user.lastName)) AS fullName, s.fileData, s.fileName FROM SemesterProject s LEFT JOIN s.user u " +
            "WHERE u.id = :currentUserId OR s.user.id = :currentUserId")
    List<SemesterProjectWrapper> getSemesterProjectsForStudent(@Param("currentUserId") Integer currentUserId);


    @Query(value = "SELECT new com.portal.wrapper.SemesterProjectWrapper (s.id, s.name, s.description, " +
            "s.term, s.status, s.createdById, s.createdBy, s.available, s.subject.id, s.subject.name, s.user.id, " +
            "CONCAT(s.user.firstName, ' ', s.user.lastName)) AS fullName, s.fileName FROM SemesterProject s" +
            " LEFT JOIN s.user where s.status = 'true' AND s.id = :id")
    SemesterProjectWrapper getPublishedProjectById(@Param("id") Integer id);

    @Transactional
    void deleteById(Integer Id);

    @Transactional
    @Modifying
    @Query("UPDATE SemesterProject s SET s.status = :status WHERE s.id =:id")
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE SemesterProject s SET s.available = :available WHERE s.id =:id")
    Integer updateAvailable(@Param("available") Boolean available, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE SemesterProject s SET s.user.id = :userId WHERE s.id =:id")
    Integer addUserId(@Param("userId") Integer userId, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE SemesterProject s SET s.user.id = null WHERE s.id =:id")
    void removeUserId(@Param("id") Integer id);

    @Override
    Optional<SemesterProject> findById(Integer integer);
}
