package com.example.springsecuritylogin.view;

import com.example.springsecuritylogin.entity.User;
import com.example.springsecuritylogin.security.SecurityConfig;
import com.example.springsecuritylogin.service.UserDetailsServiceImpl;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginLayout extends AppLayout {
    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    public LoginLayout(UserDetailsServiceImpl userService, SecurityConfig securityConfig) {
        this.userService = userService;
        passwordEncoder = securityConfig.getPasswordEncoder();
        createHeader();
    }

    public void createHeader() {
        H3 logo = new H3("Spring Security authentication");
        logo.getStyle().set("margin-left", "20px");
        logo.getStyle().set("margin-top", "11px");
        logo.addClassName("logo");
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.getElement().getThemeList().add("dark");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Button addUser = new Button("Add User", e -> createNewUserDialog());
        addUser.getStyle().set("margin-right", "20px");
        addUser.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        header.add(logo, addUser);
        addToNavbar(header);
    }

    void createNewUserDialog() {
        Binder<User> binder = new Binder<>(User.class);
        VerticalLayout dialogLayout = new VerticalLayout();
        Dialog dialog = new Dialog(dialogLayout);
        dialog.getElement().getThemeList().add("dark");
        H2 header = new H2("Create new user");
        TextField username = new TextField("Username");
        username.setAutofocus(true);
        PasswordField password = new PasswordField("Password");
        password.getStyle().set("padding-top", "0");
        EmailField emailAddress = new EmailField("Email Address");
        emailAddress.getStyle().set("padding-top", "0");
        Button cancel = new Button("Cancel", event -> dialog.close());
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button save = new Button("Save", e -> {
            User user = new User();
            if (binder.writeBeanIfValid(user)) {
                userService.saveUser(user);
                dialog.close();
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setEnabled(false);
        binder.forField(username).asRequired().withValidator(userService::isUsernameUnique, "Username is not unique").bind(User::getUsername,User::setUsername);
        binder.forField(password).asRequired().withConverter(new PasswordConverter()).bind(User::getPassword,User::setPassword);
        binder.forField(emailAddress).asRequired().withValidator(new EmailValidator("Email address is invalid")).bind(User::getEmailAddress,User::setEmailAddress);
        binder.addValueChangeListener(e -> save.setEnabled(binder.validate().isOk()));
        HorizontalLayout buttonLayout = new HorizontalLayout(cancel, save);
        buttonLayout.getStyle().set("padding-top", "15px");
        dialogLayout.add(header, username, password, emailAddress, buttonLayout);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(false);
        dialog.setResizable(true);
        dialog.open();
    }

    class PasswordConverter implements Converter<String, String> {
        @Override
        public Result<String> convertToModel(String value, ValueContext context) {
            return Result.ok(passwordEncoder.encode(value));
        }
        @Override
        public String convertToPresentation(String value, ValueContext context) {
            return value; // not used, but cannot return null
        }
    }
}
