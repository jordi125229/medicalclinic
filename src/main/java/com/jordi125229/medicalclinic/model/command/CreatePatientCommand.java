package com.jordi125229.medicalclinic.model.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientCommand {
    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Wrong email format!")
    private String email;
    @Size(min = 8)
    @NotBlank
    private String password;
    @NotBlank
    private String idCardNo;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phoneNumber;
    @NotNull
    private LocalDate birthday;
}
