package com.jordi125229.medicalclinic.model.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorCommand {
    @Email(message = "Wrong email format!")
    @NotBlank
    private String email;
    @Size(min = 8)
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String specialization;
}
