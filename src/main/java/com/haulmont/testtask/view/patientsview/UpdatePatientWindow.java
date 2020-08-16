package com.haulmont.testtask.view.patientsview;

import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.services.PatientService;
import com.haulmont.testtask.services.implementation.PatientServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class UpdatePatientWindow extends Window {
    private final TextField firstName;
    private final TextField lastName;
    private final TextField patronymic;
    private final TextField telephoneNumber;
    private final Button update;
    private final Button cancel;

    public UpdatePatientWindow(Patient patient) {
        super(ViewConstants.UPDATE_PATIENT_WINDOW_CAPTION);
        setModal(true);
        setHeight(ViewConstants.ADD_UPDATE_WINDOW_HEIGHT);
        setWidth(ViewConstants.ADD_UPDATE_WINDOW_WIDTH);
        center();

        firstName = new TextField(ViewConstants.FIRST_NAME_TEXT_FIELD_CAPTION);
        firstName.setMaxLength(30);
        firstName.setRequiredIndicatorVisible(true);
        firstName.addValueChangeListener(this::correctFieldValueValidation);

        lastName = new TextField(ViewConstants.LAST_NAME_TEXT_FIELD_CAPTION);
        lastName.setMaxLength(30);
        lastName.setRequiredIndicatorVisible(true);
        lastName.addValueChangeListener(this::correctFieldValueValidation);

        patronymic = new TextField(ViewConstants.PATRONYMIC_TEXT_FIELD_CAPTION);
        patronymic.setMaxLength(30);
        patronymic.setRequiredIndicatorVisible(true);
        patronymic.addValueChangeListener(this::correctFieldValueValidation);

        telephoneNumber = new TextField(ViewConstants.TELEPHONE_NUMBER_TEXT_FIELD_CAPTION);
        telephoneNumber.setMaxLength(15);
        telephoneNumber.setRequiredIndicatorVisible(true);
        telephoneNumber.addValueChangeListener(this::correctFieldValueValidation);

        update = new Button(ViewConstants.OK_BUTTON_CAPTION);
        update.addClickListener(event -> {
            patient.setFirstName(this.firstName.getValue());
            patient.setLastName(this.lastName.getValue());
            patient.setPatronymic(this.patronymic.getValue());
            patient.setTelephoneNumber(this.telephoneNumber.getValue());

            PatientService patientService = PatientServiceImpl.getInstance();
            patientService.update(patient);
            this.close();
            Page.getCurrent().reload();
        });

        Binder<Patient> binder = new Binder<>(Patient.class);
        binder.setBean(patient);
        binder.bindInstanceFields(this);
        binder.forField(telephoneNumber)
                .withValidator(new RegexpValidator(ViewConstants.NUMBER_FIELD_ERROR_MESSAGE,
                        ViewConstants.ONLY_DIGITS_REGEXP))
                .bind(Patient::getTelephoneNumber, Patient::setTelephoneNumber);

        cancel = new Button(ViewConstants.CANCEL_BUTTON_CAPTION);
        cancel.addClickListener(clickEvent -> this.close());


        HorizontalLayout actions = new HorizontalLayout(update, cancel);
        actions.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(firstName, lastName, patronymic,
                telephoneNumber, actions);
        mainLayout.setSpacing(true);

        setContent(mainLayout);
    }

    private <T> void correctFieldValueValidation(HasValue.ValueChangeEvent<T> changeListener) {
        if (firstName.getValue().isEmpty() || lastName.getValue().isEmpty() ||
                patronymic.getValue().isEmpty() || telephoneNumber.getValue().isEmpty() ||
                !telephoneNumber.getValue().chars().allMatch(Character::isDigit)) {
            update.setEnabled(false);
        } else {
            update.setEnabled(true);
        }
    }
}
