package com.haulmont.testtask.view.doctorsview;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.DoctorStatistics;
import com.haulmont.testtask.services.DoctorService;
import com.haulmont.testtask.services.RecipeService;
import com.haulmont.testtask.services.implementation.DoctorServiceImpl;
import com.haulmont.testtask.services.implementation.RecipeServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.util.ArrayList;
import java.util.List;

public class StatisticsWindow extends Window {
    Grid<DoctorStatistics> doctorStatisticGrid;

    public StatisticsWindow() {
        super(ViewConstants.DOCTOR_STATISTICS_WINDOW_CAPTION);
        setWidth(ViewConstants.DOCTOR_STATISTICS_WINDOW_WIDTH);
        setHeight(ViewConstants.DOCTOR_STATISTICS_WINDOW_HEIGHT);
        setModal(true);
        center();

        RecipeService recipeService = RecipeServiceImpl.getInstance();
        DoctorService doctorService = DoctorServiceImpl.getInstance();
        doctorStatisticGrid = new Grid<>(DoctorStatistics.class);
        doctorStatisticGrid.getColumn("doctorFullName").setCaption(ViewConstants.DOCTOR_COLUMN_CAPTION);
        doctorStatisticGrid.getColumn("countOfRecipes").setCaption(ViewConstants.RECIPES_COUNT_COLUMN_CAPTION);
        doctorStatisticGrid.setSizeFull();

        List<Doctor> allDoctors = doctorService.getAll();
        List<DoctorStatistics> doctorStatistics = new ArrayList<>();

        for (Doctor doctor : allDoctors) {
            DoctorStatistics currDoctorStatistics = new DoctorStatistics(doctor.getFullName(),
                    recipeService.getCountOfRecipesByDoctor(doctor));
            doctorStatistics.add(currDoctorStatistics);
        }

        doctorStatisticGrid.setItems(doctorStatistics);
        setContent(new VerticalLayout(doctorStatisticGrid));
    }
}
