package com.haulmont.testtask.services.implementation;

import com.haulmont.testtask.dao.DaoManager;
import com.haulmont.testtask.dao.RecipeDao;
import com.haulmont.testtask.dao.implementation.DaoManagerImpl;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.domain.RecipePriority;
import com.haulmont.testtask.services.RecipeService;

import java.util.Collection;
import java.util.List;

public class RecipeServiceImpl implements RecipeService {
    private final RecipeDao recipeDao;
    private static RecipeServiceImpl instance;

    private RecipeServiceImpl(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    public static synchronized RecipeServiceImpl getInstance() {
        if (instance == null) {
            DaoManager daoManager = DaoManagerImpl.getInstance();
            instance = new RecipeServiceImpl(daoManager.getRecipeDao());
        }
        return instance;
    }


    @Override
    public void save(Recipe recipe) {
        this.recipeDao.save(recipe);
    }

    @Override
    public void update(Recipe recipe) {
        this.recipeDao.update(recipe);
    }

    @Override
    public void remove(Recipe recipe) {
        this.recipeDao.remove(recipe);
    }

    @Override
    public List<Recipe> getAll() {
        return this.recipeDao.getAll();
    }

    @Override
    public void remove(Collection<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            this.remove(recipe);
        }
    }

    @Override
    public long getCountOfRecipesByDoctor(Doctor doctor) {
        return this.recipeDao.getCountOfRecipesByDoctor(doctor);
    }

    @Override
    public List<Recipe> filterByPatientName(String name) {
        return this.recipeDao.filterByPatientName(name);
    }


    @Override
    public List<Recipe> filterByDescription(String pattern) {
        return this.recipeDao.filterByDescription(pattern);
    }

    @Override
    public List<Recipe> filterByPriority(RecipePriority pattern) {
        return this.recipeDao.filterByPriority(pattern);
    }


}
