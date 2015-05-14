package org.fjorum.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "pwd_hash")
    private String passwordHash;
    @Column(name = "confirm_code")
    private String confirmationCode;
    @Column(name = "recover_pwd_code")
    private String recoverPasswordCode;
    @Column(name = "email")
    private String email;
    @Column(name = "active")
    private boolean active = true;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    protected User() {
    }

    public User(String name, String email, String passwordHash, String confirmationCode) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.confirmationCode = confirmationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getRecoverPasswordCode() {
        return recoverPasswordCode;
    }

    public void setRecoverPasswordCode(String recoverPasswordCode) {
        this.recoverPasswordCode = recoverPasswordCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
