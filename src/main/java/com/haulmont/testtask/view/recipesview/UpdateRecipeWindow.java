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

public class UpdateRecipeWindow extends Window {
    private final TextField description;
    private final ComboBox<Patient> patients;
    private final ComboBox<Doctor> doctors;
    private final TextField validity;
    private final ComboBox<RecipePriority> priorities;
    private final Button ok;
    private final Button cancel;

    public UpdateRecipeWindow(Recipe recipe) {
        super(ViewConstants.UPDATE_RECIPE_WINDOW_CAPTION);
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

        priorities = new ComboBox<>(ViewConstants.PRIORITY_FIELD_CAPTION);
        priorities.setWidthFull();
        priorities.setItems(RecipePriority.values());
        priorities.setRequiredIndicatorVisible(true);
        priorities.addValueChangeListener(this::correctFieldValueValidation);

        ok = new Button(ViewConstants.OK_BUTTON_CAPTION);

        PatientService patientService = PatientServiceImpl.getInstance();
        patients = new ComboBox<>(ViewConstants.PATIENT_FIELD_CAPTION);
        patients.setWidthFull();
        patients.setItemCaptionGenerator(Patient::getFullName);
        patients.setItems(patientService.getAll());
        patients.setRequiredIndicatorVisible(true);
        patients.addValueChangeListener(this::correctFieldValueValidation);

        DoctorService doctorService = DoctorServiceImpl.getInstance();
        doctors = new ComboBox<>(ViewConstants.DOCTOR_FIELD_CAPTION);
        doctors.setWidthFull();
        doctors.setItemCaptionGenerator(Doctor::getFullName);
        doctors.setItems(doctorService.getAll());
        doctors.setRequiredIndicatorVisible(true);
        doctors.addValueChangeListener(this::correctFieldValueValidation);


        ok.addClickListener(clickEvent -> {
            RecipeService recipeService = RecipeServiceImpl.getInstance();

            recipe.setDescription(this.description.getValue());
            recipe.setPatient(patients.getValue());
            recipe.setDoctor(doctors.getValue());
            recipe.setValidity(Integer.parseInt(this.validity.getValue()));
            recipe.setPriority(priorities.getValue());

            recipeService.update(recipe);
            this.close();
            Page.getCurrent().reload();
        });


        Binder<Recipe> binder = new Binder<>(Recipe.class);
        binder.setBean(recipe);
        binder.forField(validity)
                .withConverter(new StringToIntegerConverter(ViewConstants.NUMBER_FIELD_ERROR_MESSAGE))
                .bind(Recipe::getValidity, Recipe::setValidity);
        binder.forField(description).bind(Recipe::getDescription, Recipe::setDescription);
        binder.forField(patients).bind(Recipe::getPatient, Recipe::setPatient);
        binder.forField(doctors).bind(Recipe::getDoctor, Recipe::setDoctor);
        binder.forField(priorities).bind(Recipe::getPriority, Recipe::setPriority);

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
