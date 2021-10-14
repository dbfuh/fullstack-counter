package com.example.application.views.counterlist;

import com.example.application.data.entity.CounterListItem;
import com.example.application.data.service.CounterService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.HashMap;
import java.util.Map;

@Route(value = "")
@PageTitle("Counter list")
public class CounterlistView extends VerticalLayout {

    private final VerticalLayout shoppingList = new VerticalLayout();
    private final CounterService counterService;
    private final Map<Integer, ItemForm> forms = new HashMap<>();

    public CounterlistView(CounterService counterService) {
        addClassName("shoppinglist-view");
        this.counterService = counterService;

        var content = new HorizontalLayout(getShoppingListLayout());
        content.setSizeFull();

        setWidth(null);
        setHeightFull();
        add(getHeader(), content);
        expand(content);

        counterService.getShoppingList().forEach(this::addItem);
    }

    private Component getHeader() {
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.BASELINE);

        var h1 = new H1("Counter list");
        header.add(h1);
        header.expand(h1);
        return header;
    }

    private Component getShoppingListLayout() {
        var newItemForm = new ItemForm(new CounterListItem());
        newItemForm.setSaveHandler(item -> {
            saveItem(item);
            newItemForm.setItem(new CounterListItem());
        });
        newItemForm.addClassName("spacing-b-xl");

        var shoppingListLayout = new VerticalLayout(
            newItemForm,
            shoppingList
        );
        shoppingList.setPadding(false);
        shoppingListLayout.setHeightFull();
        return shoppingListLayout;
    }

    void addItem(CounterListItem item) {
        var form = new ItemForm(item);
        //form.setSaveHandler(this::saveItem);
        form.setIncrementHandler(this::incrementItem);

        if (item.getId() != null) {
            form.setDeleteHandler(this::deleteItem);
        }
        forms.put(item.getId(), form);
        shoppingList.add(form);
    }

    void saveItem(CounterListItem updated) {
        try {
            var newItem = updated.getId() == null;
            var saved = counterService.saveItem(updated);

            if (newItem) {
                addItem(saved);
            } else {
                forms.get(saved.getId()).setItem(saved);
            }

        } catch (ObjectOptimisticLockingFailureException e) {
            showSaveError();
        }
    }

    void incrementItem(CounterListItem item) {
        try {
            var saved = counterService.saveItem(item);
            saved.setCounter(saved.getCounter() + 1);
            forms.get(saved.getId()).setItem(saved);

        } catch (ObjectOptimisticLockingFailureException e) {
            showSaveError();
        }
    }

    void deleteItem(CounterListItem item) {
        try {
            counterService.deleteItem(item);
            shoppingList.remove(forms.get(item.getId()));
            forms.remove(item.getId());
        } catch (ObjectOptimisticLockingFailureException e) {
            showSaveError();
        }
    }

    private void showSaveError() {
        var notification = new Notification("Save conflict. Please try again.");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

}
