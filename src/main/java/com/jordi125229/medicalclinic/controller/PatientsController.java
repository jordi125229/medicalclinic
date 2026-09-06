package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.exception.ErrorMessage;
import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.model.command.UpdatePatientCommand;
import com.jordi125229.medicalclinic.service.PatientsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.TableGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
@Tag(name = "Patients")
public class PatientsController {

    private final PatientsService patientsService;

    @Operation(summary = "Get all patients.", description = "Find all patients and shows information about them.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Patients found"),
            @ApiResponse(responseCode = "400", description = "Wrong request params.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping()
    public PageableDto<PatientDto> getPatients(@RequestParam int page, @RequestParam int size) {
        return patientsService.getPatients(page, size);
    }

    @Operation(summary = "Get patient by email.", description = "Find patient by its email and shows information about it.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable("email") String email) {
        return patientsService.getPatientDto(email);
    }

    @Operation(summary = "Create a new patient.", description = "Take the information from the client and create patient basing on input.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Patient created"),
            @ApiResponse(responseCode = "400", description = "Wrong patient data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto createPatient(@Valid @RequestBody CreatePatientCommand patient) {
        return patientsService.createPatient(patient);
    }

    @Operation(summary = "Update patient.", description = "Updates the patient identified by the email address.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Patient updated."),
            @ApiResponse(responseCode = "400", description = "Invalid patient data.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Patient not found.", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PutMapping("/{email}")
    public PatientDto editPatientByEmail(@PathVariable("email") String email, @Valid @RequestBody UpdatePatientCommand patient) {
        return patientsService.editPatient(email, patient);
    }

    @Operation(summary = "Delete a patient.", description = "Delete the patient identified by the email address.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Patient's been deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientsService.deletePatient(email);
    }
}
