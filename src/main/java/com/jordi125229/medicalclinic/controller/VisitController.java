package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.command.CreateVisitCommand;
import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visits")
public class VisitController {
    private final VisitService visitService;

    @GetMapping
    public List<VisitDto> getVisits() {
        return visitService.getVisits();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDto createVisit(@Valid @RequestBody CreateVisitCommand visit) {
        return visitService.createVisit(visit);
    }

    @PatchMapping("/{email}/{id}")
    public VisitDto assignPatientToVisit(@PathVariable("email") String email, @PathVariable("id") String visitId) {
        return visitService.assignPatientToVisit(email, visitId);
    }

    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable("id") String visitId) {
        visitService.deleteVisit(visitId);
    }
}
