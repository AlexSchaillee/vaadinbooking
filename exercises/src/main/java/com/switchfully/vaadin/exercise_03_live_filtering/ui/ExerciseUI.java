package com.switchfully.vaadin.exercise_03_live_filtering.ui;

import com.switchfully.vaadin.domain.Accomodation;
import com.switchfully.vaadin.service.AccomodationService;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@SpringUI
@Theme("valo")
public class ExerciseUI extends UI {

    private Grid grid = new Grid();

    private AccomodationService accomodationService;

    @Autowired
    public ExerciseUI(AccomodationService accomodationService) {
        this.accomodationService = accomodationService;
    }

    @Override
    protected void init(VaadinRequest request) {
        List<Accomodation> accomodations = accomodationService.getAccomodations();
        populateGrid(accomodations);

        // Add a filter TextField to the top of the grid to filter the list of accomodations by name.

        TextField filter = new TextField();
        filter.setInputPrompt("Filter by name...");
//        filter.addTextChangeListener(e -> populateGrid(filterByName(accomodations, e.getText())));
        filter.addTextChangeListener(e -> applyFilter(e.getText(), (Container.Filterable) grid.getContainerDataSource()));

        // Also add a button next to the filter TextField to clear the filter.

        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> {
            filter.clear();
            populateGrid(accomodations);
        });

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filter, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        VerticalLayout mainLayout = new VerticalLayout(filtering, grid);
        mainLayout.setMargin(true);
        setContent(mainLayout);
    }

    private List<Accomodation> filterByName(List<Accomodation> accomodations, String filter) {
        return accomodations.stream()
            .filter(accomodation -> accomodation.getName().toLowerCase().contains(filter.toLowerCase()))
            .collect(toList());
    }

    private void applyFilter(String filterText, Container.Filterable filterable) {
        filterable.removeAllContainerFilters();
        filterable.addContainerFilter(new SimpleStringFilter("name", filterText, true, false));
    }

    private void populateGrid(List<Accomodation> accomodations) {
        BeanItemContainer<Accomodation> container =
                new BeanItemContainer<>(Accomodation.class, accomodations);

        container.addNestedContainerProperty("city.name");

        grid.setColumns("name", "starRating", "city.name");

        grid.getColumn("city.name").setHeaderCaption("City");

        grid.setContainerDataSource(container);
    }

}