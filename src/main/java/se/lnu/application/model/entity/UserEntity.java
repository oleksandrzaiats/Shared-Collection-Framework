package se.lnu.application.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity(name = "user")
@Table(name = "user")
public class UserEntity implements CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
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
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{3,40}$")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Pattern(regexp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,20})$/", message="Password "
    		+ "must be from 6 to 20 characters long, "
    		+ "must contain one Upper case, "
    		+ "one Lower case, "
    		+ "one special symbol: !,@,#,$,%,^,&,*,")
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
