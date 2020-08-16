package com.haulmont.testtask.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Patient {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Column(name = "patronymic")
    @NonNull
    private String patronymic;

    @Column(name = "telephone_number")
    @NonNull
    private String telephoneNumber;

    public String getFullName() {
        return lastName + " " + firstName + " " + patronymic;
    }
}
