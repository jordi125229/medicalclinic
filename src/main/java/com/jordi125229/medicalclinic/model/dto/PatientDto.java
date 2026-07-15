package com.jordi125229.medicalclinic.model.dto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {
    private Long patientId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
