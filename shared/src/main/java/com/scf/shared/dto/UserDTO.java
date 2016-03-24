package com.scf.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class UserDTO implements CommonDTO {

    private Long id;
    @NotNull
    @Size(min = 3, max = 255)
    private String login;
    @Size(max = 255)
    private String name;
    @NotNull
    /*@Pattern(regexp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,20})$/", message="Password "
            + "must be from 6 to 20 characters long, "
            + "must contain one Upper case, "
            + "one Lower case, "
            + "one special symbol: !,@,#,$,%,^,&,*,")*/
    @Size(min = 6, max = 255)
    private String password;
    @JsonIgnore
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
