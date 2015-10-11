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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            @RequestParam Optional<Long> catId,
            CurrentUser currentUser) {
        Category category = catId.
                flatMap(categoryService::getById).
                orElseGet(categoryService::getRootCategory);
        model.addAttribute("category", category);
        model.addAttribute("breadcrumbs", breadcrumbs(category.getParent()));
        model.addAttribute(TopicCreateForm.NAME,
                new TopicCreateForm(currentUser.getUser(), category));
        model.addAttribute(CategoryCreateForm.NAME, new CategoryCreateForm(category));
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
        return redirectToCategory(categoryCreateForm.getParentCategory());
    }

    @RequestMapping(value = "/topicCreate", method = RequestMethod.POST)
    public String handleTopicCreateForm(
            @Valid TopicCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "topic.create.failure");
            for(ObjectError objectError : bindingResult.getAllErrors()) {
               logger.error(objectError.toString());
            }
        } else {
            try {
                Topic topic = topicService.createNewTopic(form);
                FlashMessage.SUCCESS.put(redirectAttributes, "topic.create.success");
                return redirectToTopic(topic.getId());
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "topic.create.failure");
            }
        }
        return redirectToCategory(form.getCategory());
    }

    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public String showTopic(
            Model model,
            @RequestParam Optional<Long> topicId,
            CurrentUser currentUser,
            RedirectAttributes redirectAttributes) {
        return topicId.flatMap(topicService::getById).map(topic -> {
            model.addAttribute("topic", topic);
            model.addAttribute("breadcrumbs", breadcrumbs(topic.getCategory()));
            model.addAttribute(ReplyCreateForm.NAME,
                    new ReplyCreateForm(currentUser.getUser(), topic));
            return TOPIC_PAGE;
        }).orElseGet(() -> {
            FlashMessage.ERROR.put(redirectAttributes, "topic.show.failure");
            return redirectToCategory(null);
        });
    }

    @RequestMapping(value = "/replyCreate", method = RequestMethod.POST)
    public String handleReplyCreateForm(
            @Valid ReplyCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "reply.create.failure");
        } else {
            try {
                replyService.createNewReply(form);
                FlashMessage.SUCCESS.put(redirectAttributes, "reply.create.success");
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "reply.create.failure");
            }
        }
        return redirectToTopic(form.getTopic().getId());
    }

    private List<Category> breadcrumbs(Category category) {
        LinkedList<Category> breadcrumbs = new LinkedList<>();
        for (Category breadcrumb = category; breadcrumb != null; breadcrumb = breadcrumb.getParent()) {
            breadcrumbs.addFirst(breadcrumb);
        }
        return breadcrumbs;
    }

    private String redirectToCategory(Category category) {
        return "redirect:/forum/cat" +
                (category == null ? "" : "?catId=" + category.getId());
    }

    private String redirectToTopic(Long topicId) {
        return "redirect:/forum/topic?topicId=" + Objects.requireNonNull(topicId);
    }

}
