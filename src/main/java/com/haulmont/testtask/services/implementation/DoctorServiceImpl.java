package com.haulmont.testtask.services.implementation;

import com.haulmont.testtask.dao.DaoManager;
import com.haulmont.testtask.dao.DoctorDao;
import com.haulmont.testtask.dao.RecipeDao;
import com.haulmont.testtask.dao.implementation.DaoManagerImpl;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.services.DoctorService;
import com.haulmont.testtask.services.RemoveEntityException;

import java.util.Collection;
import java.util.List;

public class DoctorServiceImpl implements DoctorService {
    public static final String REMOVE_ENTITY_EXCEPTION_MESSAGE =
            "Impossible to remove the doctor %s because he has recipes";

    private final DoctorDao doctorDao;
    private final RecipeDao recipeDao;
    private static DoctorServiceImpl instance;

    private DoctorServiceImpl(DoctorDao doctorDao, RecipeDao recipeDao) {
        this.doctorDao = doctorDao;
        this.recipeDao = recipeDao;
    }

    public static synchronized DoctorServiceImpl getInstance() {
        if (instance == null) {
            DaoManager daoManager = DaoManagerImpl.getInstance();
            instance = new DoctorServiceImpl(daoManager.getDoctorDao(), daoManager.getRecipeDao());
        }
        return instance;
    }

    @Override
    public void save(Doctor doctor) {
        this.doctorDao.save(doctor);
    }

    @Override
    public void update(Doctor doctor) {
        this.doctorDao.update(doctor);
    }

    @Override
    public void remove(Doctor doctor) throws RemoveEntityException {
        if(this.recipeDao.getCountOfRecipesByDoctor(doctor) > 0)
            throw new RemoveEntityException(String.format(REMOVE_ENTITY_EXCEPTION_MESSAGE, doctor.getFullName()));
        this.doctorDao.remove(doctor);
    }

    @Override
    public List<Doctor> getAll() {
        return this.doctorDao.getAll();
    }

    @Override
    public void remove(Collection<Doctor> doctors) throws RemoveEntityException {
        for(Doctor doctor : doctors) {
            this.remove(doctor);
        }
    }
}
