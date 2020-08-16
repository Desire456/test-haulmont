package com.haulmont.testtask.view.patientsview;

import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.services.PatientService;
import com.haulmont.testtask.services.RemoveEntityException;
import com.haulmont.testtask.services.implementation.PatientServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Optional;
import java.util.Set;

@Theme(ValoTheme.THEME_NAME)
public class PatientsView extends VerticalLayout implements View {
    private Grid<Patient> patientGrid;
    private Button add;
    private Button remove;
    private Button update;
    private Button navigateToDoctors;
    private Button navigateToRecipes;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();
        Page.getCurrent().setTitle(ViewConstants.PATIENT_PAGE_TITLE);
        PatientService patientService = PatientServiceImpl.getInstance();

        patientGrid = new Grid<>(Patient.class);
        patientGrid.setCaption(ViewConstants.PATIENT_PAGE_TITLE);
        patientGrid.setSizeFull();
        patientGrid.setColumns("lastName", "firstName", "patronymic", "telephoneNumber");
        patientGrid.setItems(patientService.getAll());
        patientGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        patientGrid.addSelectionListener(selectionEvent -> {
            int selectedItemsSize = selectionEvent.getAllSelectedItems().size();
            remove.setEnabled(selectedItemsSize > 0);
            update.setEnabled(selectedItemsSize == 1);
        });

        add = new Button(ViewConstants.ADD_BUTTON_CAPTION, VaadinIcons.PLUS);
        add.addClickListener(clickEvent -> {
            AddPatientWindow addPatientWindow = new AddPatientWindow();
            UI.getCurrent().addWindow(addPatientWindow);
        });

        remove = new Button(ViewConstants.REMOVE_BUTTON_CAPTION, VaadinIcons.TRASH);
        remove.setEnabled(false);
        remove.addClickListener(clickEvent -> {
            Set<Patient> selectedPatients = patientGrid.getSelectedItems();
            try {
                patientService.remove(selectedPatients);
                Page.getCurrent().reload();
            } catch (RemoveEntityException e) {
                Notification.show(ViewConstants.ERROR_NOTIFICATION_CAPTION, e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        update = new Button(ViewConstants.UPDATE_BUTTON_CAPTION);
        update.setEnabled(false);
        update.addClickListener(clickEvent -> {
            Optional<Patient> optionalPatient = patientGrid.getSelectedItems().stream().findFirst();
            if (optionalPatient.isPresent()) {
                UpdatePatientWindow updatePatientWindow = new UpdatePatientWindow(optionalPatient.get());
                UI.getCurrent().addWindow(updatePatientWindow);
            }
        });

        navigateToDoctors = new Button(ViewConstants.SHOW_DOCTORS_BUTTON_CAPTION);
        navigateToDoctors.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ViewConstants.DOCTORS_VIEW_NAME));

        navigateToRecipes = new Button(ViewConstants.SHOW_RECIPES_BUTTON_CAPTION);
        navigateToRecipes.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ViewConstants.RECIPES_VIEW_NAME));

        HorizontalLayout actions = new HorizontalLayout(add, remove, update, navigateToDoctors, navigateToRecipes);

        addComponents(patientGrid, actions);
    }
}
