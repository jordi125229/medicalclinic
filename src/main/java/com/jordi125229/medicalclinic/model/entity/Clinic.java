package com.jordi125229.medicalclinic.model.entity;

import jakarta.persistence.*;

@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String number;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
