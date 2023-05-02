package com.portal.repository;


import com.portal.model.User;
import com.portal.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    @Query("SELECT u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);

//    @Query("SELECT new com.portal.wrapper.UserWrapper(u.id, u.email, u.firstName, u.lastName,  " +
//            "u.status) from User u where u.role='user'")

    @Query("select new com.portal.wrapper.UserWrapper(u.id, u.email, u.firstName, " +
            "u.lastName, u.status) from User u where u.role = 'user'")
    List<UserWrapper> getAllUsers();


    @Query("SELECT CONCAT(u.firstName, ' ', u.lastName) AS fullName from User u where u.role='teacher'")
    List<String> getAllTeachers();

    @Query("select new com.portal.wrapper.UserWrapper(u.id, CONCAT(u.firstName, ' ', u.lastName) AS name) " +
            "from User u where u.role = 'teacher'")
    List<UserWrapper> getAllTeachersWrap();


    @Query("select new com.portal.wrapper.UserWrapper(u.id, CONCAT(u.firstName, ' ', u.lastName) AS name, " +
            "u.email, u.status, u.role) from User u")
    List<UserWrapper> getAll();

    @Query("SELECT u.email from User u where u.role='admin'")
    List<String> getAllAdmins();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id =:id")
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id =:id")
    Integer updateRole(@Param("role") String status, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateRoleById(@Param("id") Integer id, @Param("role") String role);

    User findByEmail(String email);

    Optional<User> findById(Integer id);

    Integer countByRole(String role);



}
