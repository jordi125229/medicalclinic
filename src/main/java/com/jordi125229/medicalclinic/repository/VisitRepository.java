package com.jordi125229.medicalclinic.repository;

import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    boolean existsByDoctorAndVisitStartLessThanAndVisitEndGreaterThan(Doctor doctor, LocalDateTime visitEnd, LocalDateTime visitStart);

    Page<Visit> findByVisitStartGreaterThanEqualAndVisitStartLessThanAndDoctor_SpecializationIgnoreCaseAndPatientIsNull(LocalDateTime startOfDay, LocalDateTime startOfNextDay, String doctorSpecialization, Pageable pageable);

    Page<Visit> findByPatient_User_EmailIgnoreCase(String email, Pageable pageable);

    Page<Visit> findByDoctor_User_EmailIgnoreCase(String email, Pageable pageable);

    Page<Visit> findByDoctor_User_EmailIgnoreCaseAndPatientIsNull(String email, Pageable pageable);
}
