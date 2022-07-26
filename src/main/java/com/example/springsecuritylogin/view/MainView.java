package com.example.springsecuritylogin.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Main")
@PermitAll
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H2("Welcome to the main page"));
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }
}
