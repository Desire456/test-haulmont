package com.haulmont.testtask.services.implementation;

import com.haulmont.testtask.dao.DaoManager;
import com.haulmont.testtask.dao.PatientDao;
import com.haulmont.testtask.dao.RecipeDao;
import com.haulmont.testtask.dao.implementation.DaoManagerImpl;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.services.PatientService;
import com.haulmont.testtask.services.RemoveEntityException;

import java.util.Collection;
import java.util.List;

public class PatientServiceImpl implements PatientService {
    private static final String REMOVE_ENTITY_EXCEPTION_MESSAGE =
            "Impossible to remove the patient %s because he has recipes";

    private final PatientDao patientDao;
    private final RecipeDao recipeDao;
    private static PatientServiceImpl instance;

    private PatientServiceImpl(PatientDao patientDao, RecipeDao recipeDao) {
        this.patientDao = patientDao;
        this.recipeDao = recipeDao;
    }

    public static synchronized PatientServiceImpl getInstance() {
        if (instance == null) {
            DaoManager daoManager = DaoManagerImpl.getInstance();
            instance = new PatientServiceImpl(daoManager.getPatientDao(), daoManager.getRecipeDao());
        }
        return instance;
    }


    @Override
    public void save(Patient patient) {
        this.patientDao.save(patient);
    }

    @Override
    public void update(Patient patient) {
        this.patientDao.update(patient);
    }

    @Override
    public void remove(Patient patient) throws RemoveEntityException {
        if (this.recipeDao.getCountOfRecipesByPatient(patient) > 0)
            throw new RemoveEntityException(String.format(REMOVE_ENTITY_EXCEPTION_MESSAGE, patient.getFullName()));
        this.patientDao.remove(patient);
    }

    @Override
    public List<Patient> getAll() {
        return this.patientDao.getAll();
    }

    @Override
    public void remove(Collection<Patient> patients) throws RemoveEntityException {
        for (Patient patient : patients) {
            this.remove(patient);
        }
    }
}
