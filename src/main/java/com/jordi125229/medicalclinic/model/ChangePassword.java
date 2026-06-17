package com.jordi125229.medicalclinic.model;

import lombok.Data;

@Data
public class ChangePassword {
    private String password;
    private String newPassword;
}
