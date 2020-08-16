package com.haulmont.testtask.dao.implementation;

import com.haulmont.testtask.dao.PatientDao;
import com.haulmont.testtask.domain.Patient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PatientDaoImpl implements PatientDao {
    private final SessionFactory sessionFactory;

    public PatientDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Patient patient) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(patient);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Patient patient) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(patient);
        transaction.commit();
        session.close();
    }

    @Override
    public void remove(Patient patient) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(patient);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Patient> getAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Patient> allPatients = session.createQuery("from Patient ").list();
        transaction.commit();
        session.close();
        return allPatients;
    }
}
