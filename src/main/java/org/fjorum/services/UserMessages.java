package org.fjorum.services;

public interface UserMessages {

    String USER_ID = "user_id";

    String USER_LOGIN_FLASH_SUCCESS = "user.login.flash.success";
    String USER_LOGIN_FLASH_ERROR = "user.login.flash.error";

    String USER_LOGIN_EMAIL = "user.login.email";
    String USER_LOGIN_PASSWORD = "user.login.password";

    String USER_LOGIN_LOGIN_NOW_BUTTON = "user.login.login.now.button";

    String USER_LOGOUT_FLASH_SUCCESS = "user.logout.flash.success";

    String USER_REGISTER_EMAIL = "user.register.email";
    String USER_REGISTER_PASSWORD = "user.register.password";
    String USER_REGISTER_ACCEPT_TERMS_OF_SERVICE = "user.register.acceptTermsOfService";
    String USER_REGISTER_REGISTER_NOW_BUTTON = "user.register.now.button";

    String USER_REGISTER_FORM_ERROR_EMAIL = "user.register.form.error.email";
    String USER_REGISTER_FORM_ERROR_PASSWORD = "user.register.form.error.password";
    String USER_REGISTER_FORM_ERROR_ACCEPT_TERMS_OF_SERVICE = "user.register.form.error.acceptTermsOfService";

    String USER_REGISTER_SUCCESS = "user.register.success";


    String USER_ACIVATE_USER_FLASH_SUCCESS = "user.activateUser.flash.success";
    String USER_ACIVATE_USER_FLASH_ERROR = "user.activateUser.flash.error";

    String USER_REQUEST_RESET_PASSWORD_EMAIL = "user.request.reset.password.email";
    String USER_REQUEST_RESET_PASSWORD_BUTTON = "user.request.reset.password.button";
    String USER_REQUEST_RESET_PASSWORD_FLASH_SUCCESS = "user.requestResetPassword.flash.success";

    String USER_MAILER_SEND_EMAIL_WITH_ACTIVATION_CODE_SUBJECT = "user.mailer.sendEmailWithActivationCodeSubject";
    String USER_MAILER_SEND_EMAIL_WITH_ACTIVATION_CODE_BODY_TEXT = "user.mailer.sendEmailWithActivationCodeBodyText";

    String USER_RESET_PASSWORD_NEW_PASSWORD = "user.reset.password.new.password";
    String USER_RESET_PASSWORD_RESET_PASSWORD_BUTTON = "user.reset.password.reset.password.button";

    String USER_RESET_PASSWORD_FLASH_SUCCESS = "user.reset.password.flash.success";


    String USER_MAILER_SEND_EMAIL_WITH_PASSWORD_RESET_CODE_SUBJECT = "user.mailer.sendEmailWithPasswordResetCodeSubject";
    String USER_MAILER_SEND_EMAIL_WITH_PASSWORD_RESET_CODE_BODY_TEXT = "user.mailer.sendEmailWithPasswordResetCodeBodyText";


}
