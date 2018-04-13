package com.switchfully.vaadin.exercise_02_grids.ui;

import com.switchfully.vaadin.domain.Accomodation;
import com.switchfully.vaadin.service.AccomodationService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
public class BookingUI extends UI {

    private Grid grid = new Grid();
    private final TextField filter;
    private final Button addNewBtn;

    private AccomodationService accomodationService;

    @Autowired
    public BookingUI(AccomodationService accomodationService) {
        this.accomodationService = accomodationService;
        this.filter = new TextField();
        this.addNewBtn = new Button("New accomodation", FontAwesome.PLUS);
    }

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout mainLayout = new VerticalLayout(grid);

        BeanItemContainer<Accomodation> container =
                new BeanItemContainer<>(Accomodation.class, accomodationService.getAccomodations());

        container.addNestedContainerProperty("city.name");

        grid.setColumns("name", "starRating", "city.name");

        grid.getColumn("city.name").setHeaderCaption("City");

        grid.setContainerDataSource(container);

        mainLayout.setMargin(true);
        setContent(mainLayout);
    }

}