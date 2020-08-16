package com.haulmont.testtask.dao;

public interface DaoManager {
    DoctorDao getDoctorDao();

    PatientDao getPatientDao();

    RecipeDao getRecipeDao();
}
