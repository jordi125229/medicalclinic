package com.jordi125229.medicalclinic.model.command;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateClinicCommand {
    @NotBlank
    private String name;
    @NotBlank
    private String city;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String street;
    @NotBlank
    private String number;
}
