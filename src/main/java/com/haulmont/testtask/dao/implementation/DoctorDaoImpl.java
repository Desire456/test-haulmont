package com.haulmont.testtask.dao.implementation;

import com.haulmont.testtask.dao.DoctorDao;
import com.haulmont.testtask.domain.Doctor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class DoctorDaoImpl implements DoctorDao {
    private final SessionFactory sessionFactory;

    public DoctorDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Doctor doctor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(doctor);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Doctor doctor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(doctor);
        transaction.commit();
        session.close();
    }

    @Override
    public void remove(Doctor doctor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(doctor);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Doctor> getAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Doctor> allDoctors = session.createQuery("from Doctor ").list();
        transaction.commit();
        session.close();
        return allDoctors;
    }
}
