package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.exception.ErrorMessage;
import com.jordi125229.medicalclinic.model.command.CreateDoctorCommand;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.service.DoctorService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
@Tag(name = "Doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Get all doctors.", description = "Find all doctors and shows information about them.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Doctors found"),
            @ApiResponse(responseCode = "400", description = "Wrong request params.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping
    public PageableDto<DoctorDto> getDoctors(@RequestParam int page, @RequestParam int size) {
        return doctorService.getDoctors(page, size);
    }

    @Operation(summary = "Get doctor by email.", description = "Find doctor by its email and shows information about it.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/{email}")
    public DoctorDto getDoctorByEmail(@PathVariable("email") String email) {
        return doctorService.getDoctorDto(email);
    }

    @Operation(summary = "Create a new doctor.", description = "Take the information from the client and create doctor basing on input.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "400", description = "Wrong doctor data input.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto createDoctor(@Valid @RequestBody CreateDoctorCommand doctor) {
        return doctorService.createDoctor(doctor);
    }

    @Operation(summary = "Delete a doctor.", description = "Deletes the doctor identified by the email address.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Doctor's been deleted"),
            @ApiResponse(responseCode = "404", description = "Doctor not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable("email") String email) {
        doctorService.deleteDoctor(email);
    }
}
