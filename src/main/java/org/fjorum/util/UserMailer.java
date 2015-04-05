package org.fjorum.util;

import com.google.common.base.Optional;
import ninja.Context;
import ninja.Result;
import ninja.i18n.Messages;
import ninja.postoffice.Mail;
import ninja.postoffice.Postoffice;
import ninja.utils.NinjaProperties;
import org.fjorum.services.UserConstants;
import org.fjorum.services.UserMessages;

import javax.inject.Inject;
import javax.inject.Provider;

public class UserMailer {

    public static final String APPLICATION_SERVER_NAME = "application.server.name";

    @Inject
    Postoffice postoffice;

    @Inject
    Provider<Mail> mailProvider;

    NinjaProperties ninjaProperties;

    @Inject
    Messages messages;

    final String EMAIL_FROM;

    @Inject
    UserMailer(NinjaProperties ninjaProperties) {

        this.ninjaProperties = ninjaProperties;
        this.EMAIL_FROM = ninjaProperties.getOrDie(UserConstants.EMAIL_FROM_ADDRESS);

    }

    public void sendEmailWithActivationCode(
            Context context,
            String emailTo,
            String activationCode) {


        Mail mail = mailProvider.get();

        mail.addTo(emailTo);

        mail.setFrom(EMAIL_FROM);

        Optional<String> subjectText = messages.get(
                UserMessages.USER_MAILER_SEND_EMAIL_WITH_ACTIVATION_CODE_SUBJECT,
                context,
                Optional.<Result>absent());

        Optional<String> bodyText = messages.get(
                UserMessages.USER_MAILER_SEND_EMAIL_WITH_ACTIVATION_CODE_BODY_TEXT,
                context,
                Optional.<Result>absent(),
                ninjaProperties.get(APPLICATION_SERVER_NAME),
                activationCode);

        if (!bodyText.isPresent()
                || !subjectText.isPresent()) {

            return;
        }


        mail.setSubject(subjectText.get());
        mail.setBodyText(bodyText.get());

        try {

            postoffice.send(mail);

        } catch (Exception exception) {
            System.out.printf("An error occurred while sending activation email (%s)",
                    emailTo);
            //logger.error("An error occurred while sending activation email ({})",
            //        emailTo,
            //        exception);
        }

    }

    public void sendEmailWithPasswordRecoveryCode(
            Context context,
            String emailTo,
            String passwordResetCode) {

        Mail mail = mailProvider.get();

        mail.addTo(emailTo);

        mail.setFrom(EMAIL_FROM);

        Optional<String> subjectText = messages.get(
                UserMessages.USER_MAILER_SEND_EMAIL_WITH_PASSWORD_RESET_CODE_SUBJECT,
                context,
                Optional.<Result>absent());

        Optional<String> bodyText = messages.get(
                UserMessages.USER_MAILER_SEND_EMAIL_WITH_PASSWORD_RESET_CODE_BODY_TEXT,
                context,
                Optional.<Result>absent(),
                ninjaProperties.get(APPLICATION_SERVER_NAME),
                passwordResetCode);

        if (!bodyText.isPresent()
                || !subjectText.isPresent()) {
            System.out.printf("Cannot get i18n message for password reset message. "
                    + "Needs to be fixed. (Password reset for: %s)"
                    , emailTo);

            //logger.error("Cannot get i18n message for password reset message. "
            //        + "Needs to be fixed. (Password reset for: {})"
            //        , emailTo);
            return;
        }

        mail.setSubject(subjectText.get());
        mail.setBodyText(bodyText.get());

        try {
            postoffice.send(mail);
        } catch (Exception exception) {
            System.out.printf(
                    "An error occurred while resetting password (email: %s)"
                    , emailTo);
            //logger.error(
            //        "An error occurred while resetting password (email: {})"
            //        , emailTo
            //        , exception);
        }

    }


}