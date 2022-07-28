package com.example.springsecuritylogin.view;

import com.example.springsecuritylogin.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

@Route(value = "login", layout = LoginLayout.class)
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {
    private final LoginForm login;
    private String username;

    public LoginView() {
        addClassName("login-view");
        login = new LoginForm();
        login.setForgotPasswordButtonVisible(false);
        login.addForgotPasswordListener(e -> forgotPassword());
        login.getElement().getThemeList().add("dark");
        login.setAction("login");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()) {
            event.forwardTo(MainView.class);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        login.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

    private void forgotPassword() {
        //NOTE: Vaadin doesn't provide a means of getting the username from the login form
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Submit account username");
        dialog.getElement().getThemeList().add("dark");
        TextField textField = new TextField("Username");
        textField.setWidthFull();
        textField.setRequired(true);
        dialog.add(textField);
        Button cancel = new Button("Cancel", e -> dialog.close());
        Button submit = new Button("Submit", e -> {
            username = textField.getValue();
            dialog.close();
        });
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(cancel,submit);
        dialog.open();
    }
}
