package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.exception.ErrorMessage;
import com.jordi125229.medicalclinic.model.command.CreateVisitCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.service.VisitService;
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
@RequestMapping("/visits")
@Tag(name = "Visit")
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Get all visits.", description = "Find all visits and shows information about them.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Visits found"),
            @ApiResponse(responseCode = "400", description = "Wrong request params.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping
    public PageableDto<VisitDto> getVisits(@RequestParam int page, @RequestParam int size) {
        return visitService.getVisits(page, size);
    }

    @GetMapping("/{id}")
    public VisitDto getVisit(@PathVariable long id){
        return visitService.getVisit(id);
    }

    @Operation(summary = "Create a new visit.", description = "Take the information from the client and create a new visit based on the input.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Visit created"),
            @ApiResponse(responseCode = "400", description = "Wrong visit data.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDto createVisit(@Valid @RequestBody CreateVisitCommand visit) {
        return visitService.createVisit(visit);
    }

    @Operation(summary = "Assign patient to visit.", description = "Assigns the patient identified by the email address to the selected visit.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Patient assigned to visit"),
            @ApiResponse(responseCode = "404", description = "Patient or visit not found.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))), @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PatchMapping("/{email}/{id}")
    public VisitDto assignPatientToVisit(@PathVariable("email") String email, @PathVariable("id") String visitId) {
        return visitService.assignPatientToVisit(email, visitId);
    }

    @Operation(summary = "Delete a visit.", description = "Delete the visit identified by its id.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Visit has been deleted"),
            @ApiResponse(responseCode = "404", description = "Visit not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable("id") String visitId) {
        visitService.deleteVisit(visitId);
    }
}
