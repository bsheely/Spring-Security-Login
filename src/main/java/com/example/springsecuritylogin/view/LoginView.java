package com.example.springsecuritylogin.view;

import com.example.springsecuritylogin.security.SecurityUtils;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;

@Route("login")
@PageTitle("Login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver, AfterNavigationObserver {

    public LoginView() {
        addClassName("login-view");
        LoginI18n loginOverlay = LoginI18n.createDefault();
        loginOverlay.setHeader(new LoginI18n.Header());
        loginOverlay.getHeader().setTitle("Vaadin and Spring Security");
        loginOverlay.getHeader().setDescription("Username: bsheely / Password: 1234");
        loginOverlay.setAdditionalInformation("This Vaadin component does not permit adding anything else to this page. Thus, new users must be created (added to the database) elsewhere.");
        loginOverlay.setForm(new LoginI18n.Form());
        loginOverlay.getForm().setSubmit("Sign in");
        loginOverlay.getForm().setTitle("Sign in");
        loginOverlay.getForm().setUsername("Username");
        loginOverlay.getForm().setPassword("Password");
        setI18n(loginOverlay);
        setForgotPasswordButtonVisible(false);
        setAction("login");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()) {
            event.forwardTo(MainView.class);
        } else {
            setOpened(true);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
