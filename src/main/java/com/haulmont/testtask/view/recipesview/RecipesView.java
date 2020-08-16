package com.haulmont.testtask.view.recipesview;

import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.domain.RecipePriority;
import com.haulmont.testtask.services.RecipeService;
import com.haulmont.testtask.services.implementation.RecipeServiceImpl;
import com.haulmont.testtask.view.ViewConstants;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Theme(ValoTheme.THEME_NAME)
public class RecipesView extends VerticalLayout implements View {
    private Grid<Recipe> recipeGrid;
    private Grid<Recipe> filteredRecipesGrid;
    private Button add;
    private Button remove;
    private Button update;
    private Button navigateToDoctors;
    private Button navigateToPatients;
    private Button submitFilter;
    private Button cancelFilter;
    private TextField filterField;
    private ComboBox<RecipePriority> priorityComboBox;
    private ComboBox<String> filterColumns;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();
        Page.getCurrent().setTitle(ViewConstants.RECIPE_PAGE_TITLE);
        RecipeService recipeService = RecipeServiceImpl.getInstance();

        recipeGrid = new Grid<>(Recipe.class);
        filteredRecipesGrid = new Grid<>(Recipe.class);
        this.setupGridRecipeAttributes(recipeGrid);
        this.setupGridRecipeAttributes(filteredRecipesGrid);
        recipeGrid.setItems(recipeService.getAll());

        add = new Button(ViewConstants.ADD_BUTTON_CAPTION, VaadinIcons.PLUS);
        add.addClickListener(clickEvent -> {
            AddRecipeWindow addRecipeWindow = new AddRecipeWindow();
            UI.getCurrent().addWindow(addRecipeWindow);
        });

        remove = new Button(ViewConstants.REMOVE_BUTTON_CAPTION, VaadinIcons.TRASH);
        remove.setEnabled(false);
        remove.addClickListener(clickEvent -> {
            Set<Recipe> selectedRecipes = recipeGrid.getSelectedItems();
            recipeService.remove(selectedRecipes);
            Page.getCurrent().reload();
        });

        update = new Button(ViewConstants.UPDATE_BUTTON_CAPTION);
        update.setEnabled(false);
        update.addClickListener(clickEvent -> {
            Optional<Recipe> optionalRecipe = recipeGrid.getSelectedItems().stream().findFirst();
            if (optionalRecipe.isPresent()) {
                UpdateRecipeWindow updateRecipeWindow = new UpdateRecipeWindow(optionalRecipe.get());
                UI.getCurrent().addWindow(updateRecipeWindow);
            }
        });

        filterField = new TextField(ViewConstants.FILTER_TEXT_FIELD_CAPTION);
        submitFilter = new Button(ViewConstants.SUBMIT_FILTER_BUTTON_CAPTION);
        priorityComboBox = new ComboBox<>(ViewConstants.FILTER_TEXT_FIELD_CAPTION);
        priorityComboBox.setItems(RecipePriority.values());
        filterColumns = new ComboBox<>(ViewConstants.COLUMN_FIELD_CAPTION);

        cancelFilter = new Button(VaadinIcons.CLOSE_SMALL);
        cancelFilter.setVisible(false);

        HorizontalLayout filterPanel = new HorizontalLayout(filterColumns, filterField);
        filterPanel.setCaption(ViewConstants.FILTER_PANEL_CAPTION);
        HorizontalLayout filterActions = new HorizontalLayout(submitFilter, cancelFilter);

        filterColumns.setItems(ViewConstants.PATIENT_FIELD_CAPTION, ViewConstants.PRIORITY_FIELD_CAPTION,
                ViewConstants.DESCRIPTION_TEXT_FIELD_CAPTION);
        filterColumns.setValue(ViewConstants.PATIENT_FIELD_CAPTION);
        filterColumns.addValueChangeListener(changeEvent -> {
            String currColumn = changeEvent.getValue();
            submitFilter.setEnabled(currColumn != null);
            if (currColumn == null) return;
            if (currColumn.equals(ViewConstants.PRIORITY_FIELD_CAPTION)) {
                filterPanel.replaceComponent(filterField, priorityComboBox);
            } else {
                filterPanel.replaceComponent(priorityComboBox, filterField);
            }
        });

        cancelFilter.addClickListener(clickEvent -> {
            cancelFilter.setVisible(false);
            filterField.setValue("");
            filterColumns.setValue(ViewConstants.PATIENT_FIELD_CAPTION);
            submitFilter.setEnabled(true);
            replaceComponent(filteredRecipesGrid, recipeGrid);
        });

        submitFilter.addClickListener(clickEvent -> {
            String filterColumn = filterColumns.getValue();
            List<Recipe> filteredRecipes = new ArrayList<>();
            switch (filterColumn) {
                case "Patient":
                    filteredRecipes = recipeService.filterByPatientName(filterField.getValue());
                    break;
                case "Priority":
                    filteredRecipes = recipeService.filterByPriority(priorityComboBox.getValue());
                    break;
                case "Description":
                    filteredRecipes = recipeService.filterByDescription(filterField.getValue());
                    break;
            }
            filteredRecipesGrid.setItems(filteredRecipes);
            submitFilter.setEnabled(false);
            replaceComponent(recipeGrid, filteredRecipesGrid);
            cancelFilter.setVisible(true);
        });

        navigateToDoctors = new Button(ViewConstants.SHOW_DOCTORS_BUTTON_CAPTION);
        navigateToDoctors.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ViewConstants.DOCTORS_VIEW_NAME));

        navigateToPatients = new Button(ViewConstants.SHOW_PATIENTS_BUTTON_CAPTION);
        navigateToPatients.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ViewConstants.PATIENTS_VIEW_NAME));

        HorizontalLayout actions = new HorizontalLayout(add, remove, update, navigateToDoctors, navigateToPatients);

        addComponents(filterPanel, filterActions, recipeGrid, actions);
    }

    private void setupGridRecipeAttributes(Grid<Recipe> recipeGrid) {
        recipeGrid.setCaption(ViewConstants.RECIPE_PAGE_TITLE);
        recipeGrid.setSizeFull();
        recipeGrid.setColumns("description", "validity", "priority");
        recipeGrid.addColumn(Recipe::getPatientFullName).setCaption("Patient").setId("patient");
        recipeGrid.addColumn(Recipe::getDoctorFullName).setCaption("Doctor").setId("doctor");
        recipeGrid.addColumn(Recipe::getConvertedToStringCreationDate).setCaption("Creation date").setId("creationDate");
        recipeGrid.setColumnOrder("description", "patient", "doctor", "creationDate", "validity", "priority");
        recipeGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        recipeGrid.addSelectionListener(selectionEvent -> {
            int selectedItemsSize = selectionEvent.getAllSelectedItems().size();
            remove.setEnabled(selectedItemsSize > 0);
            update.setEnabled(selectedItemsSize == 1);
        });
    }
}
