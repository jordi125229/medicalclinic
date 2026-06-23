package com.jordi125229.medicalclinic.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String number;
    @ManyToMany
    @JoinTable(name = "doctor_clinic", joinColumns = @JoinColumn(name = "clinic_id"), inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    private Set<Doctor> doctors;
    @OneToMany(mappedBy = "clinic")
    private Set<Visit> visit;
}

//select *from Users;
//select * from Doctor;
//select * from Clinic;
//SELECT * FROM doctor_clinic;
