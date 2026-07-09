package com.jordi125229.medicalclinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClinicDto {
    private Long clinicId;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String number;
    private List<DoctorDto> doctorsDto;
}
