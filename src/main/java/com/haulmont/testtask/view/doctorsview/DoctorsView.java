package com.haulmont.testtask.view.doctorsview;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.services.DoctorService;
import com.haulmont.testtask.services.RemoveEntityException;
import com.haulmont.testtask.services.implementation.DoctorServiceImpl;
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
public class DoctorsView extends VerticalLayout implements View {
    private Grid<Doctor> doctorGrid;
    private Button add;
    private Button remove;
    private Button update;
    private Button showStatistics;
    private Button navigateToPatients;
    private Button navigateToRecipes;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        removeAllComponents();
        Page.getCurrent().setTitle(ViewConstants.DOCTORS_PAGE_TITLE);
        DoctorService doctorService = DoctorServiceImpl.getInstance();

        doctorGrid = new Grid<>(Doctor.class);
        doctorGrid.setCaption(ViewConstants.DOCTORS_PAGE_TITLE);
        doctorGrid.setSizeFull();
        doctorGrid.setColumns("lastName", "firstName", "patronymic", "specialization");
        doctorGrid.setItems(doctorService.getAll());
        doctorGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        doctorGrid.addSelectionListener(selectionEvent -> {
            int selectedItemsSize = selectionEvent.getAllSelectedItems().size();
            remove.setEnabled(selectedItemsSize > 0);
            update.setEnabled(selectedItemsSize == 1);
        });

        add = new Button(ViewConstants.ADD_BUTTON_CAPTION, VaadinIcons.PLUS);
        add.addClickListener(clickEvent -> {
            AddDoctorWindow addDoctorWindow = new AddDoctorWindow();
            UI.getCurrent().addWindow(addDoctorWindow);
        });

        remove = new Button(ViewConstants.REMOVE_BUTTON_CAPTION, VaadinIcons.TRASH);
        remove.setEnabled(false);
        remove.addClickListener(clickEvent -> {
            Set<Doctor> selectedDoctors = doctorGrid.getSelectedItems();
            try {
                doctorService.remove(selectedDoctors);
                Page.getCurrent().reload();
            } catch (RemoveEntityException e) {
                Notification.show(ViewConstants.ERROR_NOTIFICATION_CAPTION, e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        update = new Button(ViewConstants.UPDATE_BUTTON_CAPTION);
        update.setEnabled(false);
        update.addClickListener(clickEvent -> {
            Optional<Doctor> optionalDoctor = doctorGrid.getSelectedItems().stream().findFirst();
            if (optionalDoctor.isPresent()) {
                UpdateDoctorWindow updateDoctorWindow = new UpdateDoctorWindow(optionalDoctor.get());
                UI.getCurrent().addWindow(updateDoctorWindow);
            }
        });

        showStatistics = new Button(ViewConstants.SHOW_STATISTICS_BUTTON_CAPTION);
        showStatistics.addClickListener(clickEvent -> {
            StatisticsWindow statisticsWindow = new StatisticsWindow();
            UI.getCurrent().addWindow(statisticsWindow);
        });

        navigateToPatients = new Button(ViewConstants.SHOW_PATIENTS_BUTTON_CAPTION);
        navigateToPatients.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ViewConstants.PATIENTS_VIEW_NAME));

        navigateToRecipes = new Button(ViewConstants.SHOW_RECIPES_BUTTON_CAPTION);
        navigateToRecipes.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ViewConstants.RECIPES_VIEW_NAME));

        HorizontalLayout actions = new HorizontalLayout(add, remove, update, showStatistics, navigateToPatients,
                navigateToRecipes);

        addComponents(doctorGrid, actions);
    }
}
