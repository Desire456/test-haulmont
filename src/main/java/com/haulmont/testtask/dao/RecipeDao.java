package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.domain.RecipePriority;

import java.util.List;

public interface RecipeDao extends CrudDao<Recipe> {
    long getCountOfRecipesByDoctor(Doctor doctor);

    long getCountOfRecipesByPatient(Patient patient);

    List<Recipe> filterByPatientName(String name);

    List<Recipe> filterByDescription(String pattern);

    List<Recipe> filterByPriority(RecipePriority pattern);
}
