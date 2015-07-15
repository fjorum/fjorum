package org.fjorum.controller;

import org.fjorum.controller.form.CategoryCreateForm;
import org.fjorum.controller.form.CategoryCreateValidator;
import org.fjorum.controller.form.ReplyCreateForm;
import org.fjorum.controller.form.TopicCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.Reply;
import org.fjorum.model.entity.Topic;
import org.fjorum.model.service.CategoryService;
import org.fjorum.model.service.CurrentUser;
import org.fjorum.model.service.ReplyService;
import org.fjorum.model.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private static final String FORUM_PAGE = "forum";
    private static final String TOPIC_PAGE = "topic";
    private final CategoryService categoryService;
    private final TopicService topicService;
    private final ReplyService replyService;
    private final CategoryCreateValidator categoryCreateValidator;

    @Autowired
    public ForumController(CategoryService categoryService,
                           TopicService topicService,
                           ReplyService replyService,
                           CategoryCreateValidator categoryCreateValidator) {
        this.categoryService = categoryService;
        this.topicService = topicService;
        this.replyService = replyService;
        this.categoryCreateValidator = categoryCreateValidator;
    }

    @InitBinder(CategoryCreateForm.NAME)
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(categoryCreateValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String forum() {
        return redirectToCategory(null);
    }

    @RequestMapping(value = "/cat", method = RequestMethod.GET)
    public String showCategory(
            Model model,
            @RequestParam Optional<Long> catId) {
        Category category = catId.
                flatMap(categoryService::getById).
                orElseGet(categoryService::getRootCategory);
        model.addAttribute("category", category);
        model.addAttribute("breadcrumbs", breadcrumbs(category.getParent()));
        return FORUM_PAGE;
    }

    @RequestMapping(value = "/categoryCreate", method = RequestMethod.POST)
    public String handleCategoryCreateForm(
            @Valid CategoryCreateForm categoryCreateForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "category.create.failure");
        } else {
            try {
                categoryService.createNewCategory(categoryCreateForm);
                FlashMessage.SUCCESS.put(redirectAttributes, "category.create.success");
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "category.create.failure");
            }
        }
        return redirectToCategory(categoryCreateForm.getParentId());
    }

    @RequestMapping(value = "/topicCreate", method = RequestMethod.POST)
    public String handleTopicCreateForm(
            @Valid TopicCreateForm form,
            CurrentUser currentUser,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || currentUser == null) {
            FlashMessage.ERROR.put(redirectAttributes, "topic.create.failure");
        } else {
            try {
                Topic topic = topicService.createNewTopic(form, currentUser.getUser());
                FlashMessage.SUCCESS.put(redirectAttributes, "topic.create.success");
                return redirectToTopic(topic.getId());
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "topic.create.failure");
            }
        }
        return redirectToCategory(form.getCategoryId());
    }

    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public String showTopic(
            Model model,
            @RequestParam Optional<Long> topicId,
            RedirectAttributes redirectAttributes) {
        return topicId.flatMap(topicService::getById).map(topic -> {
            model.addAttribute("topic", topic);
            model.addAttribute("breadcrumbs", breadcrumbs(topic.getCategory()));
            return TOPIC_PAGE;
        }).orElseGet(() -> {
            FlashMessage.ERROR.put(redirectAttributes, "topic.show.failure");
            return redirectToCategory(null);
        });
    }

    @RequestMapping(value = "/replyCreate", method = RequestMethod.POST)
    public String handleReplyCreateForm(
            @Valid ReplyCreateForm form,
            CurrentUser currentUser,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || currentUser == null) {
            FlashMessage.ERROR.put(redirectAttributes, "reply.create.failure");
        } else {
            try {
                replyService.createNewReply(form, currentUser.getUser());
                FlashMessage.SUCCESS.put(redirectAttributes, "reply.create.success");
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "reply.create.failure");
            }
        }
        return redirectToTopic(form.getTopicId());
    }

    private List<Category> breadcrumbs(Category category) {
        LinkedList<Category> breadcrumbs = new LinkedList<>();
        for (Category breadcrumb = category; breadcrumb != null; breadcrumb = breadcrumb.getParent()) {
            breadcrumbs.addFirst(breadcrumb);
        }
        return breadcrumbs;
    }

    private String redirectToCategory(Long categoryId) {
        return "redirect:/forum/cat" +
                (categoryId == null ? "" : "?catId=" + categoryId);
    }

    private String redirectToTopic(Long topicId) {
        return "redirect:/forum/topic?topicId=" + Objects.requireNonNull(topicId);
    }

}
