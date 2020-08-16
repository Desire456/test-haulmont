package com.haulmont.testtask.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class SqlScriptExecutor {
    private static final String SQL_SCRIPT_EXCEPTION_MESSAGE = "Impossible to execute sql script: ";

    public static void executeSqlStartScript(String path) throws ExecuteSqlScriptException {
        try {
            String sqlScript = Files.lines(Paths.get(path)).collect(Collectors.joining());
            String[] createStatements = sqlScript.split(";");
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            for (String statement : createStatements) {
                session.createNativeQuery(statement).executeUpdate();
            }
            tx.commit();
            session.close();
        } catch (IOException e) {
            throw new ExecuteSqlScriptException(SQL_SCRIPT_EXCEPTION_MESSAGE + e.getMessage());
        }
    }
}
