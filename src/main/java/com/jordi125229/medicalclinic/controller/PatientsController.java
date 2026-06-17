package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.CommandPatient;
import com.jordi125229.medicalclinic.model.entity.ChangePassword;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.model.mapper.CommandPatientToUpdate;
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
    public PatientDto createPatient(@Valid @RequestBody CommandPatient patient) {
        return patientsService.createPatient(patient);
    }

    @PutMapping("/{email}")
    public void editPatientByEmail(@PathVariable("email") String email, @Valid @RequestBody CommandPatientToUpdate patient) {
        patientsService.editPatient(email, patient);
    }

//    @PatchMapping("/{email}/password")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void editPassword(@PathVariable("email") String email, @RequestBody ChangePassword changePassword) {
//        patientsService.changePassword(email, changePassword);
//    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientsService.deletePatient(email);
    }
}
