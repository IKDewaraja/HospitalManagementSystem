package com.hospital.patient_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // The Kafka messenger
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 1. Create a Patient (POST) with Kafka Notification
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        // First, save the patient to the database
        Patient savedPatient = patientRepository.save(patient);

        // Second, send a message to Kafka
        // Topic name: "patient-notifications"
        kafkaTemplate.send("patient-notifications", "New Patient Registered: " + savedPatient.getName());

        return savedPatient;
    }

    // ... (Keep the rest of your methods: GET, findById, DELETE)

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> ResponseEntity.ok(patient))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {
        if(!patientRepository.existsById(id)){
            return "Patient not found with id: " + id;
        }
        patientRepository.deleteById(id);
        return "Patient with id " + id + " deleted successfully.";
    }
}