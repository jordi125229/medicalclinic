package com.jordi125229.medicalclinic.model.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVisitCommand {
    @NotBlank
    private String clinicName;
    @Email(message = "Wrong email format!")
    @NotBlank
    private String doctorEmail;
    @NotNull
    private LocalDateTime visitStart;
    @NotNull
    private LocalDateTime visitEnd;
    private String patientEmail;
}
