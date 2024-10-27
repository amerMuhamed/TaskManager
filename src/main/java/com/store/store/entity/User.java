package com.store.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login_id")
    @NotNull(message = "User Name is required")
    @Size(max = 20,message = "Must be less than 20 characters")
    private String userId;

    @Column(name = "phone")
    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phone;

     @Column(name = "email")
     @NotNull(message = "Email is required")
     @Email(message = "Email should be valid")
    private String email;

    @Column(name = "pw")
    @Size(min = 6, message = "Password must be more than 6 characters")
    @NotNull(message = "Password is required")
    private String password;

    @Column(name = "active")
    private boolean enable;

    @OneToOne(mappedBy = "userId" , fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
      private List<Role> roles=new ArrayList<>();

    @Transient  // This field is not stored in the database
    private List<String> selectedRoles=new ArrayList<>();
    public User(String userId,String phone,String email, String password, boolean enable) {
        this.userId = userId;
        this.phone=phone;
        this.email=email;
        this.password = password;
        this.enable = enable;

    }

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Role> getRoles() {

        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role theRole) {
        roles.add(theRole);
        theRole.setUser(this);

    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    @Override
    public String toString() {
        return "User{" +

                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                '}';
    }

}


