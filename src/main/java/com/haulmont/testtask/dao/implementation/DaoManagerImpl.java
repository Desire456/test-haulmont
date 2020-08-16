package com.haulmont.testtask.dao.implementation;

import com.haulmont.testtask.dao.DaoManager;
import com.haulmont.testtask.dao.DoctorDao;
import com.haulmont.testtask.dao.PatientDao;
import com.haulmont.testtask.dao.RecipeDao;
import com.haulmont.testtask.utils.ExecuteSqlScriptException;
import com.haulmont.testtask.utils.HibernateSessionFactoryUtil;
import com.haulmont.testtask.utils.SqlScriptExecutor;

public class DaoManagerImpl implements DaoManager {
    private static DaoManagerImpl instance;

    private DaoManagerImpl() {
    }

    public static synchronized DaoManagerImpl getInstance() {
        if (instance == null) {
            instance = new DaoManagerImpl();
        }
        return instance;
    }

    public static synchronized DaoManagerImpl getInstance(String pathToScript) throws ExecuteSqlScriptException {
        if (instance == null) {
            instance = new DaoManagerImpl();
            executeSqlStartScript(pathToScript);
        }
        return instance;
    }

    @Override
    public DoctorDao getDoctorDao() {
        return new DoctorDaoImpl(HibernateSessionFactoryUtil.getSessionFactory());
    }

    @Override
    public PatientDao getPatientDao() {
        return new PatientDaoImpl(HibernateSessionFactoryUtil.getSessionFactory());
    }

    @Override
    public RecipeDao getRecipeDao() {
        return new RecipeDaoImpl(HibernateSessionFactoryUtil.getSessionFactory());
    }

    private static void executeSqlStartScript(String pathToScript) throws ExecuteSqlScriptException {
        SqlScriptExecutor.executeSqlStartScript(pathToScript);
    }
}
