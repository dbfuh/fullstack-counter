package com.example.application.views.counterlist;

import com.example.application.data.entity.CounterListItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;

public class ItemForm extends HorizontalLayout {

    private final IntegerField counter = new IntegerField();
    private final Binder<CounterListItem> binder;
    private final Button button = new Button("Generate");
    private CounterListItem item;

    @FunctionalInterface
    public interface SaveHandler {
        void itemSaved(CounterListItem item);
    }

    @FunctionalInterface
    public interface DeleteHandler {
        void itemDeleted(CounterListItem item);
    }

    @FunctionalInterface
    public interface IncrementHandler {
        void itemIncremented(CounterListItem item);
    }

    public ItemForm(CounterListItem item) {
        addClassName("item-form");
        setAlignItems(Alignment.BASELINE);

        counter.setReadOnly(true);
        counter.setPlaceholder("Amount");
        add(counter, button);

        binder = new Binder<>(CounterListItem.class);
        binder.bindInstanceFields(this);
        setItem(item);
    }

    public void setSaveHandler(SaveHandler saveHandler) {
        button.addClickListener(click -> {
            if (binder.writeBeanIfValid(item)) {
                saveHandler.itemSaved(item);
            }
        });
    }

    public void setIncrementHandler(IncrementHandler incrementHandler) {
        var incrementButton = new Button("Increment", e -> incrementHandler.itemIncremented(item));
        add(incrementButton);
    }

    public void setItem(CounterListItem item) {
        this.item = item;
        binder.readBean(item);

        if (item.getId() != null) {
            button.setVisible(false);
            addClassName("existing");
        } else {
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
    }

    public void setDeleteHandler(DeleteHandler deleteHandler) {
        var deleteButton = new Button("Delete", e -> deleteHandler.itemDeleted(item));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        add(deleteButton);
    }
}
