package com.haulmont.testtask.view.doctorsview;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.services.DoctorService;
import com.haulmont.testtask.services.implementation.DoctorServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class UpdateDoctorWindow extends Window {
    private final TextField firstName;
    private final TextField lastName;
    private final TextField patronymic;
    private final TextField specialization;
    private final Button update;
    private final Button cancel;

    public UpdateDoctorWindow(Doctor doctor) {
        super(ViewConstants.UPDATE_DOCTOR_WINDOW_CAPTION);
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

        update = new Button(ViewConstants.OK_BUTTON_CAPTION);
        update.addClickListener(event -> {
            doctor.setFirstName(this.firstName.getValue());
            doctor.setLastName(this.lastName.getValue());
            doctor.setPatronymic(this.patronymic.getValue());
            doctor.setSpecialization(this.specialization.getValue());

            DoctorService doctorService = DoctorServiceImpl.getInstance();
            doctorService.update(doctor);
            this.close();
            Page.getCurrent().reload();
        });

        Binder<Doctor> binder = new Binder<>(Doctor.class);
        binder.setBean(doctor);
        binder.bindInstanceFields(this);

        cancel = new Button(ViewConstants.CANCEL_BUTTON_CAPTION);
        cancel.addClickListener(clickEvent -> this.close());

        HorizontalLayout actions = new HorizontalLayout(update, cancel);
        actions.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(firstName, lastName, patronymic,
                specialization, actions);
        mainLayout.setSpacing(true);

        setContent(mainLayout);
    }

    private <T> void correctFieldValueValidation(HasValue.ValueChangeEvent<T> changeListener) {
        if (firstName.getValue().isEmpty() || lastName.getValue().isEmpty() ||
                patronymic.getValue().isEmpty() || specialization.getValue().isEmpty()) {
            update.setEnabled(false);
        } else {
            update.setEnabled(true);
        }
    }
}
