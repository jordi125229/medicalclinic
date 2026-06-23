package com.jordi125229.medicalclinic.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Patient patient;
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Doctor doctor;
}