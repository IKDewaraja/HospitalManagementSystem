package com.hospital.patient_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // 1. Create a Patient (POST)
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    // 2. Get All Patients (GET)
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // 3. Find Patient by ID (GET)
    // URL Example: http://localhost:8080/api/patients/1
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> ResponseEntity.ok(patient))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Delete Patient (DELETE)
    // URL Example: http://localhost:8080/api/patients/1
    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {
        if(!patientRepository.existsById(id)){
            return "Patient not found with id: " + id;
        }
        patientRepository.deleteById(id);
        return "Patient with id " + id + " deleted successfully.";
    }
}