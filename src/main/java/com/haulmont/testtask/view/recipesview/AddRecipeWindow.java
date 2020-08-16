package com.haulmont.testtask.view.recipesview;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.domain.RecipePriority;
import com.haulmont.testtask.services.DoctorService;
import com.haulmont.testtask.services.PatientService;
import com.haulmont.testtask.services.RecipeService;
import com.haulmont.testtask.services.implementation.DoctorServiceImpl;
import com.haulmont.testtask.services.implementation.PatientServiceImpl;
import com.haulmont.testtask.services.implementation.RecipeServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class AddRecipeWindow extends Window {
    private final TextField description;
    private final ComboBox<Patient> patients;
    private final ComboBox<Doctor> doctors;
    private final TextField validity;
    private final ComboBox<RecipePriority> priorities;
    private final Button ok;
    private final Button cancel;

    public AddRecipeWindow() {
        super(ViewConstants.ADD_RECIPE_WINDOW_CAPTION);
        setModal(true);
        setHeight(ViewConstants.ADD_UPDATE_RECIPE_WINDOW_HEIGHT);
        setWidth(ViewConstants.ADD_UPDATE_RECIPE_WINDOW_WIDTH);
        center();

        description = new TextField(ViewConstants.DESCRIPTION_TEXT_FIELD_CAPTION);
        description.setWidthFull();
        description.setRequiredIndicatorVisible(true);
        description.addValueChangeListener(this::correctFieldValueValidation);

        validity = new TextField(ViewConstants.VALIDITY_TEXT_FIELD_CAPTION);
        validity.setWidthFull();
        validity.setRequiredIndicatorVisible(true);
        validity.addValueChangeListener(this::correctFieldValueValidation);

        ok = new Button(ViewConstants.OK_BUTTON_CAPTION);
        ok.setEnabled(false);

        Binder<Recipe> binder = new Binder<>(Recipe.class);
        binder.forField(validity)
                .withConverter(new StringToIntegerConverter(ViewConstants.NUMBER_FIELD_ERROR_MESSAGE))
                .bind(Recipe::getValidity, Recipe::setValidity);

        priorities = new ComboBox<>(ViewConstants.PRIORITY_FIELD_CAPTION);
        priorities.setItems(RecipePriority.values());
        priorities.setWidthFull();
        priorities.setRequiredIndicatorVisible(true);
        priorities.addValueChangeListener(this::correctFieldValueValidation);

        PatientService patientService = PatientServiceImpl.getInstance();
        patients = new ComboBox<>(ViewConstants.PATIENT_FIELD_CAPTION);
        patients.setWidthFull();
        patients.setItemCaptionGenerator(Patient::getFullName);
        patients.setItems(patientService.getAll());
        patients.setRequiredIndicatorVisible(true);
        patients.addValueChangeListener(this::correctFieldValueValidation);


        DoctorService doctorService = DoctorServiceImpl.getInstance();
        doctors = new ComboBox<>(ViewConstants.DOCTOR_COLUMN_CAPTION);
        doctors.setWidthFull();
        doctors.setItemCaptionGenerator(Doctor::getFullName);
        doctors.setItems(doctorService.getAll());
        doctors.setRequiredIndicatorVisible(true);
        doctors.addValueChangeListener(this::correctFieldValueValidation);

        ok.addClickListener(clickEvent -> {
            RecipeService recipeService = RecipeServiceImpl.getInstance();

            String description = this.description.getValue();
            Patient patient = patients.getValue();
            Doctor doctor = doctors.getValue();
            int validity = Integer.parseInt(this.validity.getValue());
            RecipePriority recipePriority = priorities.getValue();

            Recipe toSave = new Recipe(description, patient, doctor, validity, recipePriority);
            recipeService.save(toSave);
            this.close();
            Page.getCurrent().reload();
        });
        cancel = new Button(ViewConstants.CANCEL_BUTTON_CAPTION);
        cancel.addClickListener(clickEvent -> this.close());

        VerticalLayout mainLayout = new VerticalLayout(description, patients, doctors, validity, priorities);
        HorizontalLayout actions = new HorizontalLayout(ok, cancel);
        mainLayout.addComponent(actions);
        setContent(mainLayout);
    }


    private <T> void correctFieldValueValidation(HasValue.ValueChangeEvent<T> changeListener) {
        if (description.getValue().isEmpty() || !patients.getSelectedItem().isPresent() ||
                !doctors.getSelectedItem().isPresent() || validity.getValue().isEmpty() ||
                !priorities.getSelectedItem().isPresent() || !validity.getValue().chars().allMatch(Character::isDigit)) {
            ok.setEnabled(false);
        } else {
            ok.setEnabled(true);
        }
    }
}
