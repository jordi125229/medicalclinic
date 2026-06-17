package com.jordi125229.medicalclinic.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String specialization;
    @OneToMany(mappedBy = "doctor")
    private Set<Clinic> clinics;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
