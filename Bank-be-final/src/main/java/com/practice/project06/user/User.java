package com.practice.project06.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private Long userID;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    public User() {}

    public User(User other) {
        if (other != null) {
            this.userID = other.userID;
            this.username = other.username;
            this.password = other.password;
            this.role = other.role;
            this.email = other.email;
            this.address = other.address;
        }
    }
}
