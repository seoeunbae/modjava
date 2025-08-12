package com.example.shoppingcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private String pincode;
    private String role;

    public User() {
    }

    public User(String name, String email, String password, String mobile, String address, String pincode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.pincode = pincode;
    }

    public User(String name, String email, String password, String mobile, String address, String pincode, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.pincode = pincode;
        this.role = role;
    }
}
