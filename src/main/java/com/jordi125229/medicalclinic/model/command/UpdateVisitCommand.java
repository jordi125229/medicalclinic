package com.jordi125229.medicalclinic.model.command;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UpdateVisitCommand {
    @NotBlank
    private Clinic clinic;
    @Email(message = "Wrong email format!")
    @NotBlank
    private Doctor doctor;
    @NotNull
    private LocalDateTime visitStart;
    @NotNull
    private LocalDateTime visitEnd;
}
