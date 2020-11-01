package com.example.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class User {
    @Id
    private String account;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "verify_code")
    private String verifyCode;
}
