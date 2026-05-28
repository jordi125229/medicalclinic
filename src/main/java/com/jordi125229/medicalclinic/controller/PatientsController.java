package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.ChangePassword;
import com.jordi125229.medicalclinic.model.Patient;
import com.jordi125229.medicalclinic.service.PatientsService;
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Patient> getPatients() {
        return patientsService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable("email") String email) {
        return patientsService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient createPatient(@RequestBody Patient patient) {
        return patientsService.createPatient(patient);
    }

    @PatchMapping("/{email}")
    public Patient editPatientByEmail(@PathVariable("email") String email, @RequestBody Patient patient) {
        return patientsService.editPatient(email, patient);
    }

    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.OK)
    public void editPassword(@PathVariable("email") String email, @RequestBody ChangePassword changePassword) {
        patientsService.changePassword(email, changePassword);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable("email") String email) {
        patientsService.deletePatient(email);
    }
}
