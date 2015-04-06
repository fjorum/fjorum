package org.fjorum.controllers;

import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;
import ninja.params.Param;
import ninja.session.FlashScope;
import ninja.session.Session;
import ninja.validation.Length;
import ninja.validation.Required;
import ninja.validation.Validation;
import org.fjorum.controllers.annotation.Get;
import org.fjorum.controllers.annotation.Post;
import org.fjorum.models.User;
import org.fjorum.services.UserMessages;
import org.fjorum.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Inject
    UserService userService;

    @Get("/")
    @Get("/index")
    @UnitOfWork
    public Result index() {
        return Results.html();
    }

    @Post("/index")
    @UnitOfWork
    public Result login(Session session, FlashScope flashScope, Validation validation,
                        @Param("emailOrName") @Required @Length(min = 1) String emailOrName,
                        @Param("password") @Required @Length(min = 1) String passwordPlainText) {

        return (validation.hasViolations()
                ? Optional.<User>empty()
                : userService.findUserByEmailOrName(emailOrName)).

                filter(user -> userService.isPasswordValid(user, passwordPlainText)).

                map(user -> {
                    session.put(UserMessages.USER_ID, user.getId().toString());
                    flashScope.success(UserMessages.USER_LOGIN_FLASH_SUCCESS);
                    return Results.redirect("/index");
                }).

                orElseGet(() -> {
                    flashScope.put("email", emailOrName);
                    flashScope.error(UserMessages.USER_LOGIN_FLASH_ERROR);
                    return Results.redirect("/index");
                });
    }

    @Get("/index/logout")
    @UnitOfWork
    public Result logout(FlashScope flashScope, Session session) {
        session.clear();
        flashScope.success(UserMessages.USER_LOGOUT_FLASH_SUCCESS);
        return Results.redirect("/index");
    }

}