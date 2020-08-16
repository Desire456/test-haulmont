package com.haulmont.testtask.view.doctorsview;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.services.DoctorService;
import com.haulmont.testtask.services.implementation.DoctorServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.data.HasValue;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class AddDoctorWindow extends Window {
    private final TextField firstName;
    private final TextField lastName;
    private final TextField patronymic;
    private final TextField specialization;
    private final Button ok;
    private final Button cancel;


    public AddDoctorWindow() {
        super(ViewConstants.ADD_DOCTOR_WINDOW_CAPTION);
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

        specialization = new TextField(ViewConstants.SPECIALIZATION_TEXT_FIELD_CAPTION);
        specialization.setMaxLength(20);
        specialization.setRequiredIndicatorVisible(true);
        specialization.addValueChangeListener(this::correctFieldValueValidation);

        ok = new Button(ViewConstants.OK_BUTTON_CAPTION);
        ok.setEnabled(false);
        ok.addClickListener(event -> {
            String firstName = this.firstName.getValue();
            String lastName = this.lastName.getValue();
            String patronymic = this.patronymic.getValue();
            String specialization = this.specialization.getValue();

            Doctor toSave = new Doctor(firstName, lastName, patronymic, specialization);
            DoctorService doctorService = DoctorServiceImpl.getInstance();
            doctorService.save(toSave);
            this.close();
            Page.getCurrent().reload();
        });

        cancel = new Button(ViewConstants.CANCEL_BUTTON_CAPTION);
        cancel.addClickListener(clickEvent -> this.close());


        HorizontalLayout actions = new HorizontalLayout(ok, cancel);
        actions.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(firstName, lastName, patronymic,
                specialization, actions);
        mainLayout.setSpacing(true);

        setContent(mainLayout);
    }

    private <T> void correctFieldValueValidation(HasValue.ValueChangeEvent<T> changeListener) {
        if (firstName.getValue().isEmpty() || lastName.getValue().isEmpty() ||
                patronymic.getValue().isEmpty() || specialization.getValue().isEmpty()) {
            ok.setEnabled(false);
        } else {
            ok.setEnabled(true);
        }
    }
}
