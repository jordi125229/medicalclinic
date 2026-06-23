package com.jordi125229.medicalclinic.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitCommand {
    private String clinicName;
    private String doctorEmail;
    private LocalDateTime visitStart;
    private LocalDateTime visitEnd;
    private String patientEmail;
}
