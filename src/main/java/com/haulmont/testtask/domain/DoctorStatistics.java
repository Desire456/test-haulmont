package com.haulmont.testtask.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class DoctorStatistics {
    @NonNull
    private String doctorFullName;

    @NonNull
    private long countOfRecipes;
}
