package com.haulmont.testtask.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
public class Recipe {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(name = "validity")
    private Integer validity;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private RecipePriority priority;

    public Recipe(String description, Patient patient, Doctor doctor, Integer validity,
                  RecipePriority priority) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.creationDate = LocalDateTime.now();
        this.validity = validity;
        this.priority = priority;
    }

    public String getPatientFullName() {
        return patient.getFullName();
    }

    public String getDoctorFullName() {
        return doctor.getFullName();
    }

    public String getConvertedToStringCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm");
        return creationDate.format(formatter);
    }
}
