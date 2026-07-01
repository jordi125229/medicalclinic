package com.jordi125229.medicalclinic.model.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Long patientId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
