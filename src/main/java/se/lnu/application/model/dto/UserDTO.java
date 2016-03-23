package se.lnu.application.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO implements CommonDTO {

    private Long id;
    
    private String login;
    private String name;
    
    private String password;
    @JsonIgnore
    private String role;
    
    @NotNull
    @Min(1)
    @Max(value = Long.MAX_VALUE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @NotNull
    @Pattern(regexp="/^[a-z0-9_-]{3,20}$/")
    public String getLogin() {
        return login;
    }

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{3,40}$")
    public String getName() {
        return name;
    }

    @JsonIgnore
    @Pattern(regexp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,20})$/", message="Password "
    		+ "must be from 6 to 20 characters long, "
    		+ "must contain one Upper case, "
    		+ "one Lower case, "
    		+ "one special symbol: !,@,#,$,%,^,&,*,")
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
