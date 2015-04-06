package org.fjorum.controllers;

import com.google.inject.persist.Transactional;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;
import ninja.session.Session;
import org.fjorum.controllers.annotations.Get;
import org.fjorum.controllers.annotations.Post;
import org.fjorum.controllers.extractors.LoggedInUser;
import org.fjorum.controllers.filters.LoggedInFilter;
import org.fjorum.controllers.filters.ModAdminAuthorizationFilter;
import org.fjorum.models.Category;
import org.fjorum.models.Topic;
import org.fjorum.models.User;
import org.fjorum.services.CategoryService;
import org.fjorum.services.TopicService;
import org.fjorum.services.UserService;
import org.fjorum.util.Optionals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class ForumController {

    private Logger logger = LoggerFactory.getLogger(ForumController.class);

    @Inject
    private UserService userService;

    @Inject
    private CategoryService categoryService;

    @Inject
    private TopicService topicService;

    @Get("/forum")
    @UnitOfWork
    public Result forum(Session session, @LoggedInUser Optional<User> user) {
        return Results.html().
                render("categories", categoryService.findAllCategories()).
                render("user", user.orElse(User.GUEST));
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
            Optional<Category> category = categoryService.findCategoryById(categoryId);
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
            Optional<Category> category = categoryService.findCategoryById(categoryId);
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
            Category category = categoryService.findCategoryById(categoryId).get();
            List<Topic> topics = topicService.findTopicByCategory(category);
            return Results.html().render("category", category).render("topics", topics);
        } catch (Exception ex) {
            logger.error("selected unknown category", ex);
            context.getFlashScope().error("forum.category.flash.error");
            return Results.redirect("/forum");
        }
    }

    @Post("/forum/topicCreate")
    @UnitOfWork
    @FilterWith(LoggedInFilter.class)
    public Result topicCreate(Context context) {
        try {
            Long categoryId = Long.valueOf(context.getParameter("category_id"));
            String name = context.getParameter("name");
            return Optionals.lift2((Category c, User u) -> topicCreateTX(c, u, name)).
                    apply(categoryService.findCategoryById(categoryId),
                            userService.findUserBySession(context.getSession())).
                    map(topic -> Results.redirect("/forum/topic?id=" + topic.getId())).orElse(
                    Results.redirect("/forum")
            );
        } catch (Exception ex) {
            logger.error("ups...", ex);
        }
        return Results.redirect("/forum");
    }

    //We need a separate, non-private method in order to make it transactional,
    //as we can't redirect to a page for a topic which isn't committed yet.
    @Transactional
    Topic topicCreateTX(Category category, User user, String name) {
        return topicService.createNewTopic(category, user, name);
    }

    @Get("/forum/topic")
    @UnitOfWork
    public Result topic(Context context) {
        try {
            Long topicId = Long.valueOf(context.getParameter("id"));
            Topic topic = topicService.findTopicById(topicId).get();
            return Results.html().render("topic", topic);
        } catch (Exception ex) {
            logger.error("selected unknown topic", ex);
            context.getFlashScope().error("forum.topic.flash.error");
            return Results.redirect("/forum");
        }
    }

}
