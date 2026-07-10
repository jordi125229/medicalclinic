package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.model.command.UpdatePatientCommand;
import com.jordi125229.medicalclinic.service.PatientsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientsController {
    private final PatientsService patientsService;

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientsService.getPatients();
    }

    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable("email") String email) {
        return patientsService.getPatientDto(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto createPatient(@Valid @RequestBody CreatePatientCommand patient) {
        return patientsService.createPatient(patient);
    }

    @PutMapping("/{email}")
    public void editPatientByEmail(@PathVariable("email") String email, @Valid @RequestBody UpdatePatientCommand patient) {
        patientsService.editPatient(email, patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientsService.deletePatient(email);
    }
}
