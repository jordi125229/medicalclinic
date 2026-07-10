package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService clinicService;

    @GetMapping
    public List<ClinicDto> getClinics() {
        return clinicService.getClinics();
    }

    @GetMapping("/{name}")
    public ClinicDto getClinicByName(@PathVariable("name") String name) {
        return clinicService.getClinicDto(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicDto createClinic(@Valid @RequestBody CreateClinicCommand clinic) {
        return clinicService.createClinic(clinic);
    }

    @PatchMapping("/{email}/clinics/{name}")
    public void assignDoctorToClinic(@PathVariable("email") String email, @PathVariable("name") String name) {
        clinicService.assignDoctorToClinic(email, name);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClinic(@PathVariable("name") String name) {
        clinicService.deleteClinic(name);
    }
}
