package com.jordi125229.medicalclinic.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDto {
    private Long id;
    private ClinicDtoWithoutDoctors clinicDto;
    private String doctorEmail;
    private LocalDateTime visitStart;
    private LocalDateTime visitEnd;
    private String patientEmail;
}
