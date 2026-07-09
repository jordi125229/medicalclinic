package com.jordi125229.medicalclinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitDto {
    private ClinicDto clinicDto;
    private String doctorEmail;
    private LocalDateTime visitStart;
    private LocalDateTime visitEnd;
    private String patientEmail;
}
