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
import org.fjorum.controllers.extractors.LoggedInUser;
import org.fjorum.controllers.filters.LoggedInFilter;
import org.fjorum.controllers.filters.ModAdminAuthorizationFilter;
import org.fjorum.models.Category;
import org.fjorum.models.Topic;
import org.fjorum.models.User;
import org.fjorum.services.CategoryService;
import org.fjorum.services.ReplyService;
import org.fjorum.services.TopicService;
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
    private CategoryService categoryService;

    @Inject
    private TopicService topicService;

    @Inject
    private ReplyService replyService;

    @Get("/forum")
    @UnitOfWork
    public Result forum(@LoggedInUser Optional<User> user) {
        return Results.html().
                render("categories", categoryService.findAllCategories()).
                render("user", user.orElse(User.GUEST));
    }

    @Post("/forum/categoryCreate")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result categoryCreate(
            @Param("name") String name) {
        categoryService.createNewCategory(name);
        return Results.redirect("/forum");
    }

    @Post("/forum/categoryUp")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result categoryUp(
            @Param("category_id") String categoryId) {
        try {
            categoryService.
                    findCategoryById(Long.valueOf(categoryId)).
                    ifPresent(categoryService::up);
        } catch (Exception ex) {
            logger.error("category up for unknown category", ex);
        }
        return Results.redirect("/forum");
    }


    @Post("/forum/categoryDown")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result categoryDown(
            @Param("category_id") String categoryId) {
        try {
            categoryService.
                    findCategoryById(Long.valueOf(categoryId)).
                    ifPresent(categoryService::down);
        } catch (Exception ex) {
            logger.error("category down for unknown category", ex);
        }
        return Results.redirect("/forum");
    }


    @Get("/forum/category")
    @UnitOfWork
    public Result category(
            FlashScope flashScope,
            @Param("id") String categoryId,
            @LoggedInUser Optional<User> user) {
        try {
            Category category = categoryService.findCategoryById(Long.valueOf(categoryId)).get();
            List<Topic> topics = topicService.findTopicByCategory(category);
            return Results.html().
                    render("category", category).
                    render("topics", topics).
                    render("user", user.orElse(User.GUEST));
        } catch (Exception ex) {
            logger.error("selected unknown category", ex);
            flashScope.error("forum.category.flash.error");
            return Results.redirect("/forum");
        }
    }

    @Post("/forum/topicCreate")
    @UnitOfWork
    @FilterWith(LoggedInFilter.class)
    public Result topicCreate(
            @Param("category_id") String categoryId,
            @Param("name") String name,
            @LoggedInUser Optional<User> user) {
        try {
            return Optionals.lift2((Category c, User u) -> topicCreateTX(c, u, name)).
                    apply(categoryService.findCategoryById(Long.valueOf(categoryId)), user).
                    map(topic -> Results.redirect("/forum/topic?id=" + topic.getId())).
                    orElse(Results.redirect("/forum"));
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
    public Result topic(
            FlashScope flashScope,
            @Param("id") String topicId) {
        return topicService.findTopicById(Long.valueOf(topicId)).
                map(topic ->
                        Results.html().
                                render("topic", topic).
                                render("replies", replyService.findRepliesByTopic(topic))).
                orElseGet(() -> {
                    flashScope.error("forum.topic.flash.error");
                    return Results.redirect("/forum");
                });
    }

    @Post("/forum/replyCreate")
    @Transactional
    @FilterWith(LoggedInFilter.class)
    public Result replyCreate(
            @Param("topic_id") String topicId,
            @Param("content") String content,
            @LoggedInUser Optional<User> user) {
        try {
            return Optionals.lift2((Topic t, User u) -> replyService.createNewReply(t, u, content)).
                    apply(topicService.findTopicById(Long.valueOf(topicId)), user).
                    map(reply -> Results.redirect("/forum/topic?id=" + topicId)).
                    orElse(Results.redirect("/forum"));
        } catch (Exception ex) {
            logger.error("ups...", ex);
        }
        return Results.redirect("/forum");
    }

    @Post("/forum/topicSwitchOpen")
    @Transactional
    @FilterWith(ModAdminAuthorizationFilter.class)
    public Result topicSwitchOpen(
            @Param("topic_id") String topicId) {
        try {
            return topicService.findTopicById(Long.valueOf(topicId)).map(topic -> {
                topic.setOpen(!topic.isOpen());
                topicService.save(topic);
                return Results.redirect("/forum/category?id=" + topic.getCategory().getId());
            }).orElseGet(() -> Results.redirect("/forum"));
        } catch (Exception ex) {
            logger.error("ups...", ex);
        }
        return Results.redirect("/forum");
    }


}
