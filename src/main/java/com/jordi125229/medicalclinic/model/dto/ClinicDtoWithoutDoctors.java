package com.jordi125229.medicalclinic.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClinicDtoWithoutDoctors {
    private Long clinicId;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String number;
}
