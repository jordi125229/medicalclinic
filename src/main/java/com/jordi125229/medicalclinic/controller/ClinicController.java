package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
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

    @GetMapping()
    public PageableDto<ClinicDto> getClinics(@RequestParam int page, @RequestParam int size) {
        return clinicService.getClinics(page, size);
    }

    @GetMapping("/{id}")
    public ClinicDto getClinicByName(@PathVariable Long id) {
        Clinic clinicById = clinicService.getClinicById(id);
        return clinicService.getClinicDto(clinicById.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicDto createClinic(@Valid @RequestBody CreateClinicCommand clinic) {
        return clinicService.createClinic(clinic);
    }

    @PatchMapping("/{email}/{id}")
    public ClinicDto assignDoctorToClinic(@PathVariable String email, @PathVariable Long id) {
        return clinicService.assignDoctorToClinic(email, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
    }
}
