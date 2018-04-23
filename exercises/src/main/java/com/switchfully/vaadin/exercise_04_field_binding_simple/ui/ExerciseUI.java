package com.switchfully.vaadin.exercise_04_field_binding_simple.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.ui.AbstractTextField.TextChangeEventMode.EAGER;
import static com.vaadin.ui.AbstractTextField.TextChangeEventMode.LAZY;

@SpringUI
@Theme("valo")
public class ExerciseUI extends UI {

    private Button update;
    private TextField field;
    private FieldEvents.TextChangeListener autoCommitTextChangeListener =
            e -> field.getPropertyDataSource().setValue(e.getText());

    @Autowired
    public ExerciseUI() {

    }

    @Override
    protected void init(VaadinRequest request) {
        // Create a TextField and a Label, both bound to the same 'name' Property.
        // Add a button to commit the field.
        // Clicking the button should update the Label with the value in the TextField.

        // Extra credits: Add a checkbox to hide the button and make the TextField auto-commit.

        Property nameProperty = new ObjectProperty<String>("");

        field = new TextField("Name", nameProperty);
        field.setInputPrompt("Fill in name");
//        field.setNullRepresentation("");
        field.setBuffered(true);
        Label label = new Label(nameProperty);

        CheckBox toggleAutoCommit = new CheckBox("Auto commit?");
        toggleAutoCommit.addValueChangeListener(e -> {
            Object value = e.getProperty().getValue();
            boolean isCheck = (null == value) ? false : (Boolean) value;
            if (isCheck) {
                enableAutoCommit();
            } else {
                disableAutoCommit();
            }
        });

        update = new Button("Update");
        update.addClickListener(e -> field.commit());

        VerticalLayout mainLayout = new VerticalLayout(field, label, toggleAutoCommit, update);
        mainLayout.setMargin(true);
        setContent(mainLayout);
    }

    private void enableAutoCommit() {
        update.setVisible(false);
//        field.setTextChangeEventMode(EAGER);
        field.addTextChangeListener(autoCommitTextChangeListener);
    }

    private void disableAutoCommit() {
        update.setVisible(true);
//        field.setTextChangeEventMode(LAZY);
        field.removeTextChangeListener(autoCommitTextChangeListener);
    }

}