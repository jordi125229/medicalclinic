package com.jordi125229.medicalclinic.controller;
import com.jordi125229.medicalclinic.model.command.CreateDoctorCommand;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public PageableDto<DoctorDto> getDoctors(@RequestParam int page, @RequestParam int size) {
        return doctorService.getDoctors(page, size);
    }

    @GetMapping("/{email}")
    public DoctorDto getDoctorByEmail(@PathVariable("email") String email) {
        return doctorService.getDoctorDto(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto createDoctor(@Valid @RequestBody CreateDoctorCommand doctor) {
        return doctorService.createDoctor(doctor);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable("email") String email) {
        doctorService.deleteDoctor(email);
    }
}
