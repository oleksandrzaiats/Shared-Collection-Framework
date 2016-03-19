package se.lnu.application.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO implements CommonDTO {

    private Long id;
    @NotNull
    @Size(min = 3, max = 255)
    private String login;
    private String name;
    @NotNull
    @Size(min = 3, max = 255)
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

    @JsonIgnore
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
