package com.portal.serviceImpl;

import com.google.common.base.Strings;
import com.portal.JWT.JwtFilter;
import com.portal.JWT.JwtUtil;
import com.portal.constents.PortalConstants;
import com.portal.model.User;
import com.portal.repository.UserRepository;
import com.portal.service.UserService;
import com.portal.utils.PortalUtils;
import com.portal.utils.EmailUtils;
import com.portal.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(requestMap));
                    return PortalUtils.getResponseEntity("Úspešná registrácia.", HttpStatus.OK);
                } else {
                    return PortalUtils.getResponseEntity("Email už existuje.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return PortalUtils.getResponseEntity(PortalConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (userDetailServiceImpl.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(userDetailServiceImpl.getUserDetail().getEmail(),
                                    userDetailServiceImpl.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex.getCause());
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials." + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userRepository.getAllUsers(), HttpStatus.OK);
                //ak si admin, tak mi vrát zoznam userov
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
    public ResponseEntity<List<String>> getAllTeachers() {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                return new ResponseEntity<>(userRepository.getAllTeachers(), HttpStatus.OK);
                //ak si admin, tak mi vrát zoznam userov
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
    public ResponseEntity<List<UserWrapper>> getAll() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userRepository.getAll(), HttpStatus.OK);
                //ak si admin, tak mi vrát zoznam userov
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
    public ResponseEntity<List<UserWrapper>> getAllTechersWrap() {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isTeacher()) {
                return new ResponseEntity<>(userRepository.getAllTeachersWrap(), HttpStatus.OK);
                //ak si admin, tak mi vrát zoznam userov
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
    public ResponseEntity<String> getNameCurrentUser() {
        String userEmail = jwtFilter.getCurrentUser();
        User user = userRepository.findByEmailId(userEmail);
        String userName = user.getFirstName() + " " + user.getLastName();
        return PortalUtils.getResponseEntity(user.getFirstName() + " " + user.getLastName(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userRepository.findById(Integer.valueOf(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    //parsujeme to na intiger, inak by to vyhodilo chybu
                    //sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepository.getAllAdmins());
                    //tu odosielame mail všetkým adminom, že niektorý z adminov zmenil niekomu status
                    return PortalUtils.getResponseEntity("Status používateľa bol úspešne zmenený.", HttpStatus.OK);
                    //musíš byť admin a status true, aby si niekomu inému mohol meniť status
                } else {
                    PortalUtils.getResponseEntity("ID používateľa nebolo nájdené.", HttpStatus.OK);
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
    public ResponseEntity<String> updateRole(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userRepository.findById(Integer.valueOf(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    userRepository.updateRole(requestMap.get("role"), Integer.parseInt(requestMap.get("id")));
                    //parsujeme to na intiger, inak by to vyhodilo chybu
                    //sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepository.getAllAdmins());
                    //tu odosielame mail všetkým adminom, že niektorý z adminov zmenil niekomu status
                    return PortalUtils.getResponseEntity("Rola používateľa bola úspešne zmenená.", HttpStatus.OK);
                    //musíš byť admin a status true, aby si niekomu inému mohol meniť status
                } else {
                    PortalUtils.getResponseEntity("ID používateľa nebolo nájdené.", HttpStatus.OK);
                }
            } else {
                return PortalUtils.getResponseEntity(PortalConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:- " + user +
                    " \n is approved by \nADMIN:- " + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "USER:- " + user +
                    " \n is disabled by \nADMIN:- " + jwtFilter.getCurrentUser(), allAdmin);
        }
    }*/

    @Override
    public ResponseEntity<String> checkToken() {

        return PortalUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)) {
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    //táto podmienka by nám mala kontrolovať či sme zadali správne staré heslo...
                    userObj.setPassword(requestMap.get("newPassword"));
                    userRepository.save(userObj);
                    //zmení heslo
                    return PortalUtils.getResponseEntity("Heslo bolo úspešne zmenené.", HttpStatus.OK);
                }
                return PortalUtils.getResponseEntity("Nesprávne staré heslo.", HttpStatus.BAD_REQUEST);
            }
            return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(), "Prihlasovacie údaje z Portálu semestrálnych prác.", user.getPassword());
            //ak sa email zhoduje s tým, ktorý je v uvedený v Userovi, tak naň zašle heslo, ak užívateľ zadal iný,
            // neodošle sa nič
            return PortalUtils.getResponseEntity("Skontrolujte si svoju poštu, či nemáte poverenia.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("firstName") && requestMap.containsKey("lastName")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        //ak by som chcela aby mi vkladal rovno inú rolu, tak by som musela dať requestMap.get("role");
        return user;
    }
}
