package com.switchfully.vaadin.exercise_05_binding_beans.ui.components;

import com.switchfully.vaadin.domain.Accomodation;
import com.switchfully.vaadin.service.AccomodationService;
import com.switchfully.vaadin.service.CityService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

import static com.switchfully.vaadin.domain.Accomodation.AccomodationBuilder.accomodation;
import static java.util.stream.Collectors.toList;

public class AccomodationAdmin extends CustomComponent {

    private Grid grid = new Grid();

    private AccomodationService accomodationService;
    private CityService cityService;
    private TextField filter;
    private Button newAccomodationButton;
    private BeanItemContainer<Accomodation> container;

    public AccomodationAdmin(AccomodationService accomodationService, CityService cityService) {
        this.accomodationService = accomodationService;
        this.cityService = cityService;

        List<Accomodation> accomodations = accomodationService.getAccomodations();
        populateGrid(accomodations);
        CssLayout filtering = createFilterComponent();

        // Add a form to the right of the grid to edit details of an accomodation.

        EditAccomodationForm form = new EditAccomodationForm(this, accomodationService, cityService);
        form.setVisible(false);

        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSpacing(true);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        grid.addSelectionListener(event -> {
            if (event.getSelected().isEmpty()) {
                form.setVisible(false);
            } else {
                Accomodation accomodation = (Accomodation) event.getSelected().iterator().next();
                form.setAccomodation(accomodation);
            }
        });

        newAccomodationButton = new Button("Add new accomodation");
        newAccomodationButton.addClickListener(e -> form.setAccomodation(accomodation().build()));

        HorizontalLayout toolbar = new HorizontalLayout(filtering, newAccomodationButton);
        toolbar.setSpacing(true);


        VerticalLayout mainLayout = new VerticalLayout(toolbar, main);
        mainLayout.setMargin(true);
        setCompositionRoot(mainLayout);
    }

    private CssLayout createFilterComponent() {
        filter = new TextField();
        filter.setInputPrompt("Filter by name...");
        filter.addTextChangeListener(e -> {
            container.removeAllContainerFilters();
            container.addContainerFilter(new SimpleStringFilter("name", e.getText(), true, false));
        });

        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> {
            filter.clear();
            container.removeAllContainerFilters();
        });

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filter, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        return filtering;
    }

    private void populateGrid(List<Accomodation> accomodations) {
        container = new BeanItemContainer<>(Accomodation.class, accomodations);

        container.addNestedContainerProperty("city.name");

        grid.setColumns("name", "starRating", "city.name");

        grid.getColumn("city.name").setHeaderCaption("City");

        grid.setContainerDataSource(container);
    }

    public void updateList() {
        filter.clear();
        populateGrid(accomodationService.getAccomodations());
    }

}
