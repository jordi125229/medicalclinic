package com.jordi125229.medicalclinic.repository;

import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    boolean existsByDoctorAndVisitStartLessThanAndVisitEndGreaterThan(Doctor doctor, LocalDateTime visitEnd, LocalDateTime visitStart);
}
