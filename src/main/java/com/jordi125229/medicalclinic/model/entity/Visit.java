package com.jordi125229.medicalclinic.model.entity;

import com.jordi125229.medicalclinic.model.command.UpdateVisitCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private LocalDateTime visitStart;
    private LocalDateTime visitEnd;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


}
