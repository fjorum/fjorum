package org.fjorum.controllers;

import com.google.inject.persist.Transactional;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;
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
    public Result userCreate(Context context) {
        try {
            boolean success = userService.createNewUser(
                    context.getParameter("name"),
                    context.getParameter("email"),
                    context.getParameter("password"),
                    null);
            return success ? Results.redirect("/admin") : Results.redirect("/errors");
        } catch (ServiceException ex) {
            logger.error("oops", ex);
            return Results.redirect("/errors");
        }
    }

    @Post("/admin/userRemove")
    @Transactional
    @FilterWith(AdminAuthorizationFilter.class)
    public Result userRemove(Context context) {
        try {
            Long userId = Long.valueOf(context.getParameter("userId"));
            userService.removeUser(userId);
            return Results.redirect("/admin");
        } catch (ServiceException ex) {
            logger.error("oops", ex);
            return Results.redirect("/errors");
        }
    }

    @Post("/admin/userChange")
    @Transactional
    @FilterWith(AdminAuthorizationFilter.class)
    public Result userChange(Context context) {
        try {
            Long userId = Long.valueOf(context.getParameter("userId"));
            userService.findUserById(userId).ifPresent(user -> {
                user.setName(context.getParameter("name"));
                user.setEmail(context.getParameter("email"));
                user.setActive(context.getParameter("active") != null);
                user.setModerator(context.getParameter("moderator") != null);
                user.setAdministrator(context.getParameter("administrator") != null);
                userService.save(user);
            });
            return Results.redirect("/admin");
        } catch (ServiceException ex) {
            logger.error("oops", ex);
            return Results.redirect("/errors");
        }
    }

}
