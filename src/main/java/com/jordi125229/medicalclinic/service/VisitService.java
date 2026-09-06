package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.*;
import com.jordi125229.medicalclinic.model.command.CreateVisitCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.entity.Visit;
import com.jordi125229.medicalclinic.model.mapper.VisitMapper;
import com.jordi125229.medicalclinic.repository.ClinicRepository;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public PageableDto<VisitDto> getVisits(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Visit> visitPage = visitRepository.findAll(pageable);
        List<VisitDto> visits = visitPage.stream()
                .map(visitMapper::visitToDto)
                .toList();
        return PageableDto.create(visits, visitPage);
    }

    public PageableDto<VisitDto> getVisitsForPatient(int pageNumber, int pageSize, String email) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Visit> visitPage = visitRepository.findByPatient_User_EmailIgnoreCase(email, pageable);
        List<VisitDto> visits = visitPage.stream()
                .map(visitMapper::visitToDto)
                .toList();
        return PageableDto.create(visits, visitPage);
    }

    public PageableDto<VisitDto> getVisitsForDoctor(int pageNumber, int pageSize, String email) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Visit> visitPage = visitRepository.findByDoctor_User_EmailIgnoreCase(email, pageable);
        List<VisitDto> visits = visitPage.stream()
                .map(visitMapper::visitToDto)
                .toList();
        return PageableDto.create(visits, visitPage);
    }

    public PageableDto<VisitDto> getAvailableVisitsForDoctor(int pageNumber, int pageSize, String doctorEmail) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Visit> visitPage = visitRepository.findByDoctor_User_EmailIgnoreCaseAndPatientIsNull(doctorEmail, pageable);
        List<VisitDto> visits = visitPage.stream()
                .map(visitMapper::visitToDto)
                .toList();
        return PageableDto.create(visits, visitPage);
    }

    public VisitDto getVisit(long visitId) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new NoVisitException("Can't find visit", HttpStatus.NOT_FOUND));
        return visitMapper.visitToDto(visit);
    }

    public PageableDto<VisitDto> getVisitsForDayByDoctorSpecialization(int pageNumber, int pageSize, LocalDate day, String doctorSpecialization) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        LocalDateTime startOfDay = day.atStartOfDay();
        LocalDateTime startOfNextDay = day.plusDays(1).atStartOfDay();
        Page<Visit> visitPage = visitRepository.findByVisitStartGreaterThanEqualAndVisitStartLessThanAndDoctor_SpecializationIgnoreCaseAndPatientIsNull(startOfDay, startOfNextDay, doctorSpecialization, pageable);

        List<VisitDto> visits = visitPage.getContent()
                .stream()
                .map(visitMapper::visitToDto)
                .toList();
        return PageableDto.create(visits, visitPage);
    }

    @Transactional
    public VisitDto createVisit(CreateVisitCommand createVisitCommand) {
        Clinic clinic = clinicRepository.findByName(createVisitCommand.getClinicName())
                .orElseThrow(() -> new NoClinicException("Can't find clinic!", HttpStatus.NOT_FOUND));
        Doctor doctor = doctorRepository.findByUserEmail(createVisitCommand.getDoctorEmail())
                .orElseThrow(() -> new NoDoctorException("Doctor wasn't found!", HttpStatus.NOT_FOUND));
        Patient patient = null;
        patient = checkIfPatientExists(createVisitCommand, patient);
        visitStartValidation(createVisitCommand);
        if (!(createVisitCommand.getVisitStart().getMinute() % 15 == 0)) {
            throw new WrongTimeException("Appointments can only begin on the quarter-hour mark", HttpStatus.BAD_REQUEST);
        }
        boolean visitOverlap = visitRepository.existsByDoctorAndVisitStartLessThanAndVisitEndGreaterThan(doctor, createVisitCommand.getVisitEnd(), createVisitCommand.getVisitStart());
        if (visitOverlap) {
            throw new WrongTimeException("Visit are overlapping!", HttpStatus.BAD_REQUEST);
        }
        Visit visit = new Visit(null, clinic, doctor, createVisitCommand.getVisitStart(), createVisitCommand.getVisitEnd(), patient);
        visitRepository.save(visit);
        return visitMapper.visitToDto(visit);
    }

    @Transactional
    public VisitDto assignPatientToVisit(String email, String visitId) {
        long idValue = Long.parseLong(visitId);
        Visit visit = visitRepository.findById(idValue)
                .orElseThrow(() -> new NoVisitException("Can't find visit", HttpStatus.NOT_FOUND));
        visitValidation(visit);
        Patient patient = getPatient(email);
        visit.setPatient(patient);
        visitRepository.save(visit);
        return visitMapper.visitToDto(visit);
    }

    @Transactional
    public void deleteVisit(String visitId) {
        long idValue = Long.parseLong(visitId);
        Visit visit = visitRepository.findById(idValue)
                .orElseThrow(() -> new NoVisitException("Can't find visit", HttpStatus.NOT_FOUND));
        visitRepository.delete(visit);
    }

    private Patient getPatient(String createVisitCommand) {
        return patientRepository.findByUserEmail(createVisitCommand)
                .orElseThrow(() -> new PatientNotFoundException("Patient wasn't found!", HttpStatus.NOT_FOUND));
    }

    private static void visitValidation(Visit visit) {
        if (!(visit.getPatient() == null)) {
            throw new NoVisitException("Visit is already booked!", HttpStatus.BAD_REQUEST);
        }
    }

    private Patient checkIfPatientExists(CreateVisitCommand createVisitCommand, Patient patient) {
        if (createVisitCommand.getPatientEmail() != null) {
            patient = getPatient(createVisitCommand.getPatientEmail());
        }
        return patient;
    }

    private static void visitStartValidation(CreateVisitCommand createVisitCommand) {
        if (createVisitCommand.getVisitStart().isAfter(createVisitCommand.getVisitEnd())) {
            throw new WrongTimeException("Start time can't be after end time!", HttpStatus.BAD_REQUEST);
        }
    }
}
