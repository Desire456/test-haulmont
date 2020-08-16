package com.haulmont.testtask.dao.implementation;

import com.haulmont.testtask.dao.RecipeDao;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.domain.RecipePriority;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RecipeDaoImpl implements RecipeDao {
    private final SessionFactory sessionFactory;

    public RecipeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Recipe recipe) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(recipe);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Recipe recipe) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(recipe);
        transaction.commit();
        session.close();
    }

    @Override
    public void remove(Recipe recipe) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(recipe);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Recipe> getAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Recipe> allRecipes = session.createQuery("from Recipe ").list();
        transaction.commit();
        session.close();
        return allRecipes;
    }

    @Override
    public long getCountOfRecipesByDoctor(Doctor doctor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Recipe r where r.doctor = :doctor");
        query.setParameter("doctor", doctor);
        long countOfRecipes = (long) query.getSingleResult();
        transaction.commit();
        session.close();
        return countOfRecipes;
    }

    @Override
    public long getCountOfRecipesByPatient(Patient patient) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select count(*) from Recipe recipe where recipe.patient = :patient");
        query.setParameter("patient", patient);
        long countOfRecipes = (long) query.getSingleResult();
        transaction.commit();
        session.close();
        return countOfRecipes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Recipe> filterByPatientName(String name) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hqlQuery = "from Recipe r where concat(r.patient.lastName, ' ', r.patient.firstName, ' '," +
                "r.patient.patronymic) like concat('%', :name, '%')";
        Query query = session.createQuery(hqlQuery);
        query.setParameter("name", name);
        List<Recipe> filteredRecipes = query.list();
        transaction.commit();
        session.close();
        return filteredRecipes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Recipe> filterByDescription(String pattern) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hqlQuery = "from Recipe where description like concat('%', :pattern, '%')";
        Query query = session.createQuery(hqlQuery).setParameter("pattern", pattern);
        List<Recipe> filteredRecipes = query.list();
        transaction.commit();
        session.close();
        return filteredRecipes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Recipe> filterByPriority(RecipePriority pattern) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hqlQuery = "from Recipe where priority = :pattern";
        Query query = session.createQuery(hqlQuery).setParameter("pattern", pattern);
        List<Recipe> filteredRecipes = query.list();
        transaction.commit();
        session.close();
        return filteredRecipes;
    }
}
