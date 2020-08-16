package com.haulmont.testtask.view;

import com.haulmont.testtask.dao.implementation.DaoManagerImpl;
import com.haulmont.testtask.utils.ExecuteSqlScriptException;
import com.haulmont.testtask.view.doctorsview.DoctorsView;
import com.haulmont.testtask.view.patientsview.PatientsView;
import com.haulmont.testtask.view.recipesview.RecipesView;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class ViewNavigator extends UI {
    Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        navigator = new Navigator(this, this);

        try {
            DaoManagerImpl.getInstance(ViewConstants.PATH_TO_SQL_START_SCRIPT);
        } catch (ExecuteSqlScriptException e) {
            Notification.show(ViewConstants.ERROR_NOTIFICATION_CAPTION, e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

        navigator.addView(ViewConstants.PATIENTS_VIEW_NAME, new PatientsView());
        navigator.addView(ViewConstants.DOCTORS_VIEW_NAME, new DoctorsView());
        navigator.addView(ViewConstants.RECIPES_VIEW_NAME, new RecipesView());
    }
}
