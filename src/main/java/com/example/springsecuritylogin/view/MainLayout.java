package com.example.springsecuritylogin.view;

import com.example.springsecuritylogin.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
    }

    public void createHeader() {
        H3 logo = new H3("Vaadin/Spring Security demo");
        logo.getStyle().set("margin-left", "20px");
        logo.getStyle().set("margin-top", "11px");
        logo.addClassName("logo");
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.getElement().getThemeList().add("dark");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Button logout = new Button("Logout", click -> securityService.logout());
        logout.getStyle().set("margin-right", "20px");
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Span authenticatedUser = new Span(securityService.getAuthenticatedUser().getUsername());
        HorizontalLayout buttonLayout = new HorizontalLayout(authenticatedUser, logout);
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.add(logo, buttonLayout);
        addToNavbar(header);
    }
}
