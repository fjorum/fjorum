package org.fjorum.controllers;

import com.google.common.io.BaseEncoding;
import com.google.inject.persist.Transactional;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.session.FlashScope;
import ninja.validation.JSR303Validation;
import ninja.validation.Length;
import ninja.validation.Required;
import ninja.validation.Validation;
import org.fjorum.annotation.Get;
import org.fjorum.annotation.Post;
import org.fjorum.services.UserMessages;
import org.fjorum.services.UserService;
import org.fjorum.util.UserMailer;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class RegisterController {

    Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Inject
    UserService userService;

    @Inject
    UserMailer userMailer;


    ///////////////////////////////////////////////////////////////////////////
    // Register new user
    ///////////////////////////////////////////////////////////////////////////
    @Get("/register")
    @UnitOfWork
    public Result register() {

        return Results.html();

    }

    @Post("/register")
    @Transactional
    public Result registerPost(
            Context context,
            FlashScope flashScope,
            Validation validation,
            @JSR303Validation RegisterForm registerForm) {

        // check if user already exists in db.
        boolean userAlreadyExists = false;

        if (!"".equals(registerForm.email)) {

            userAlreadyExists
                    = userService.doesUserExist(registerForm.email);

        }

        if (validation.hasBeanViolations()
                || userAlreadyExists) {

            registerForm.clearErrorMessages();

            if (validation.hasBeanViolation(RegisterForm.EMAIL)
                    || userAlreadyExists) {

                registerForm.emailErrorMessage
                        = UserMessages.USER_REGISTER_FORM_ERROR_EMAIL;

            }

            if (validation.hasBeanViolation(RegisterForm.PASSWORD)) {
                registerForm.passwordErrorMessage
                        = UserMessages.USER_REGISTER_FORM_ERROR_PASSWORD;

            }

            if (validation.hasBeanViolation(RegisterForm.ACCEPT_TERMS_OF_SERVICE)) {
                registerForm.acceptTermsOfServiceErrorMessage
                        = UserMessages.USER_REGISTER_FORM_ERROR_ACCEPT_TERMS_OF_SERVICE;

            }

            return Results
                    .html()
                    .template("views/CasinoController/register.ftl.html")
                    .render(registerForm);

        } else {

            String activationCode = createWebSafeUuid();

            userService.createNewUser(
                    registerForm.name,
                    registerForm.email,
                    registerForm.password,
                    activationCode);

            userMailer.sendEmailWithActivationCode(
                    context,
                    registerForm.email,
                    activationCode);

            flashScope.success(UserMessages.USER_REGISTER_SUCCESS);
            return Results.redirect("/");

        }

    }

    public Result activateUser(
            FlashScope flashScope,
            @PathParam("activationCode") String activationCode) {

        return userService.findUserByConfirmationCode(activationCode).map(user -> {
            user.setConfirmationCode(null);
            userService.save(user);

            flashScope.success(UserMessages.USER_ACIVATE_USER_FLASH_SUCCESS);
            return Results.noContent().redirect("/user/login");
        }).orElseGet(() -> {
            flashScope.error(UserMessages.USER_ACIVATE_USER_FLASH_ERROR);
            return Results.noContent().redirect("/");
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Reset password
    ///////////////////////////////////////////////////////////////////////////
    public Result requestResetPassword() {

        return Results.html();

    }

    public Result requestResetPasswordPost(
            Context context,
            FlashScope flashScope,
            Validation validation,
            @Param("email") @Required @Length(min = 3) String email) {

        if (email != null) {

            userService.findUserByEmail(email).ifPresent(user -> {

                String passwordRecoveryCode = createWebSafeUuid();
                user.setRecoverPasswordCode(passwordRecoveryCode);
                userService.save(user);

                userMailer.sendEmailWithPasswordRecoveryCode(
                        context,
                        email,
                        passwordRecoveryCode);

            });

        }

        // This message might be wrong because we don't want anyone to
        // brute force check registered users.
        flashScope.success(UserMessages.USER_REQUEST_RESET_PASSWORD_FLASH_SUCCESS);

        return Results.noContent().redirect("/");

    }

    public Result resetPassword(
            @PathParam("passwordResetCode") String passwordResetCode) {

        return Results.html().render("passwordResetCode", passwordResetCode);

    }

    public Result resetPasswordPost(
            FlashScope flashScope,
            @Param("passwordResetCode") String passwordResetCode,
            @Param("password") String password) {

        userService.findUserByRecoveryPasswordCode(passwordResetCode).ifPresent(user -> {
            userService.setNewPassword(user, password);
            user.setRecoverPasswordCode(null);
            userService.save(user);
        });

        flashScope.success(UserMessages.USER_RESET_PASSWORD_FLASH_SUCCESS);

        return Results.noContent().redirect("/user/login");

    }

    // Just a helper that mimicks the form to create new user.
    public static class RegisterForm {

        @NotBlank
        @NotNull
        @Size(min = 1, max = 256)
        public String name;
        public static final String NAME = "name";


        @NotBlank
        @NotNull
        @Email
        public String email;
        public static final String EMAIL = "email";

        public String emailErrorMessage;

        @NotBlank
        @NotNull
        @Size(min = 8, max = 25)
        public String password;
        public static final String PASSWORD = "password";

        public String passwordErrorMessage;

        @NotNull
        public Boolean acceptTermsOfService;
        public static final String ACCEPT_TERMS_OF_SERVICE = "acceptTermsOfService";

        public String acceptTermsOfServiceErrorMessage;

        public RegisterForm() {
        }

        public void clearErrorMessages() {
            emailErrorMessage = null;
            passwordErrorMessage = null;
        }

    }

    private String createWebSafeUuid() {

        try {

            UUID uuid = UUID.randomUUID();
            byte[] uuidAsBytes = uuid.toString().getBytes("UTF-8");
            return BaseEncoding.base64Url().encode(uuidAsBytes);

        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            String message = "There is something really wrong. We cannot use UTF-8 encoding. Stopping.";
            logger.error(message, unsupportedEncodingException);
            throw new RuntimeException(
                    message,
                    unsupportedEncodingException);
        }

    }

}