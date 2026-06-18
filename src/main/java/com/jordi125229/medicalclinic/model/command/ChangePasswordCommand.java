package com.jordi125229.medicalclinic.model.command;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordCommand {
    private String password;
    @Size(min = 8)
    private String newPassword;
}
