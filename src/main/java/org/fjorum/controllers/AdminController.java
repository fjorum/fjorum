package org.fjorum.controllers;

import com.google.inject.persist.Transactional;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;
import ninja.params.Param;
import ninja.session.FlashScope;

import org.fjorum.controllers.annotations.Get;
import org.fjorum.controllers.annotations.Post;
import org.fjorum.controllers.filters.AdminAuthorizationFilter;
import org.fjorum.services.ServiceException;
import org.fjorum.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Inject
    private UserService userService;

    @Get("/admin")
    @UnitOfWork
    @FilterWith(AdminAuthorizationFilter.class)
    public Result admin() {
        return Results.html().render("users", userService.findAllUsers());
    }

    @Post("/admin/userCreate")
    @Transactional
    @FilterWith(AdminAuthorizationFilter.class)
    public Result userCreate(
            @Param("name") String name,
            @Param("email") String email,
            @Param("password") String password) {
        try {
            return userService.createNewUser(name, email, password, null)
                    ? Results.redirect("/admin")
                    : Results.redirect("/errors");
        } catch (ServiceException ex) {
            logger.error("oops", ex);
            return Results.redirect("/errors");
        }
    }

    @Post("/admin/userRemove")
    @Transactional
    @FilterWith(AdminAuthorizationFilter.class)
    public Result userRemove(
            @Param("userId") String userId) {
        try {
            userService.removeUser(Long.valueOf(userId));
            return Results.redirect("/admin");
        } catch (ServiceException ex) {
            logger.error("oops", ex);
            return Results.redirect("/errors");
        }
    }

    @Post("/admin/userChange")
    @Transactional
    @FilterWith(AdminAuthorizationFilter.class)
    public Result userChange(
            @Param("userId") String userId,
            @Param("name") String name,
            @Param("email") String email,
            @Param("active") String active,
            @Param("moderator") String moderator,
            @Param("administrator") String administrator) {
        try {
            userService.findUserById(Long.valueOf(userId)).ifPresent(user -> {
                user.setName(name);
                user.setEmail(email);
                user.setActive(active != null);
                user.setModerator(moderator != null);
                user.setAdministrator(administrator != null);
                userService.save(user);
            });
            return Results.redirect("/admin");
        } catch (ServiceException ex) {
            logger.error("oops", ex);
            return Results.redirect("/errors");
        }
    }

}
