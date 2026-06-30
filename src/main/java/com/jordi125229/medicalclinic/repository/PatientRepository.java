package com.jordi125229.medicalclinic.repository;

import com.jordi125229.medicalclinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserEmail(String email);

    Optional<Patient> findByUserEmailAndIdNot(String email, Long id);
}
