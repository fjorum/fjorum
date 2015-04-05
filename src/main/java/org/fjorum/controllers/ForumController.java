package org.fjorum.controllers;

import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;
import ninja.session.FlashScope;
import ninja.session.Session;
import org.fjorum.annotation.Get;
import org.fjorum.annotation.Post;
import org.fjorum.filters.ModAdminAuthorizationFilter;
import org.fjorum.models.Category;
import org.fjorum.models.User;
import org.fjorum.services.CategoryService;
import org.fjorum.services.UserMessages;
import org.fjorum.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

@Singleton
public class ForumController {

    private Logger logger = LoggerFactory.getLogger(ForumController.class);

    @Inject
    private UserService userService;

    @Inject
    CategoryService categoryService;

    @Get("/forum")
    @UnitOfWork
    public Result forum(Session session) {
        User user = Optional.ofNullable(session.get(UserMessages.USER_ID)).
                map(Long::valueOf).
                flatMap(userService::findUserById).orElse(User.GUEST);

        return Results.html().
                render("categories", categoryService.findAllCategories()).
                render("user", user);
    }

    @Post("/forum/categoryCreate")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result categoryCreate(Context context) {
        categoryService.createNewCategory(context.getParameter("name"));
        return Results.redirect("/forum");
    }

    @Post("/forum/categoryUp")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result categoryUp(Context context) {
        try {
            Long categoryId = Long.valueOf(context.getParameter("category_id"));
            Optional<Category> category = categoryService.findCategory(categoryId);
            category.ifPresent(categoryService::up);
        } catch (Exception ex) {
            logger.error("category up for unknown category", ex);
        }
        return Results.redirect("/forum");
    }


    @Post("/forum/categoryDown")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result categoryDown(Context context) {
        try {
            Long categoryId = Long.valueOf(context.getParameter("category_id"));
            Optional<Category> category = categoryService.findCategory(categoryId);
            category.ifPresent(categoryService::down);
        } catch (Exception ex) {
            logger.error("category down for unknown category", ex);
        }
        return Results.redirect("/forum");
    }



    @Get("/forum/category")
    @UnitOfWork
    public Result category(Context context) {
        try {
            Long categoryId = Long.valueOf(context.getParameter("id"));
            Category category = categoryService.findCategory(categoryId).get();
            return Results.html().render("category", category);
        } catch (Exception ex) {
            logger.error("selected unknown category", ex);
            context.getFlashScope().error("forum.category.flash.error");
            return Results.redirect("/forum");
        }
    }

}
