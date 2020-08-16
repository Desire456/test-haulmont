package com.haulmont.testtask.services;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.domain.RecipePriority;

import java.util.Collection;
import java.util.List;

public interface RecipeService extends CrudService<Recipe> {
    void remove(Collection<Recipe> collection);

    long getCountOfRecipesByDoctor(Doctor doctor);

    List<Recipe> filterByPatientName(String name);

    List<Recipe> filterByDescription(String pattern);

    List<Recipe> filterByPriority(RecipePriority pattern);
}
