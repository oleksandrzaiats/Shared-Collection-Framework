package com.scf.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO implements CommonDTO {

    @Min(1)
    @Max(value = Long.MAX_VALUE)
    private Long id;
    @NotNull
    @Pattern(regexp="/^[a-z0-9_-]{3,20}$/")
    private String login;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{3,40}$")
    private String name;
    @NotNull
    @Pattern(regexp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,20})$/", message="Password "
            + "must be from 6 to 20 characters long, "
            + "must contain one Upper case, "
            + "one Lower case, "
            + "one special symbol: !,@,#,$,%,^,&,*,")
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
