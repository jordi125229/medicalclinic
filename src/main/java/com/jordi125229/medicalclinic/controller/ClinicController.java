package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.exception.ErrorMessage;
import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinics")
@Tag(name = "Clinics")
public class ClinicController {

    private final ClinicService clinicService;

    @Operation(summary = "Get all clinics", description = "Find all clinics and shows information about them.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Clinics found"),
            @ApiResponse(responseCode = "400", description = "Wrong request params.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping
    public PageableDto<ClinicDto> getClinics(@RequestParam int page, @RequestParam int size) {
        return clinicService.getClinics(page, size);
    }

    @Operation(summary = "Get clinic by id.", description = "Find clinic by its id and shows information about it.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Clinic found"),
            @ApiResponse(responseCode = "404", description = "Clinic not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/{id}")
    public ClinicDto getClinicById(@PathVariable Long id) {
        return clinicService.getClinicDto(id);
    }

    @Operation(summary = "Create a new clinic.", description = "Take the information from the client and create clinic basing on input.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Clinic created"),
            @ApiResponse(responseCode = "400", description = "Wrong clinic data input.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicDto createClinic(@Valid @RequestBody CreateClinicCommand clinic) {
        return clinicService.createClinic(clinic);
    }

    @Operation(summary = "Assign doctor to actual clinic.", description = "User can assign doctor to clinic. Clinic can have more doctors than one.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Doctor assigned"),
            @ApiResponse(responseCode = "404", description = "Clinic or doctor not found.", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PatchMapping("/{email}/{id}")
    public ClinicDto assignDoctorToClinic(@PathVariable String email, @PathVariable Long id) {
        return clinicService.assignDoctorToClinic(email, id);
    }

    @Operation(summary = "Delete a clinic.", description = "Delete the clinic identified by the id.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Clinic's been deleted"),
            @ApiResponse(responseCode = "404", description = "Clinic not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
    }
}
