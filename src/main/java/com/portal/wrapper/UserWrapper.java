package com.portal.wrapper;

import com.portal.model.User;
import jakarta.persistence.Converter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@Data - táto anotácia sa nám stara o gettery a settery, čiže nemusíme ich vobec pisat a vieme ich pouziť
@NoArgsConstructor
public class UserWrapper {
    private Integer id;
    private String firstName;
    private String lastName;
    private String name = firstName + " " + lastName;
    private String email;
    private String status;
    private String role;


    public UserWrapper(Integer id, String name, String email, String status, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.role = role;
    }

    public UserWrapper(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
