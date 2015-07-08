package org.fjorum.controller;

import org.fjorum.controller.form.CategoryCreateForm;
import org.fjorum.controller.form.CategoryCreateValidator;
import org.fjorum.controller.form.TopicCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.Topic;
import org.fjorum.model.service.CategoryService;
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
import java.util.Optional;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private final CategoryService categoryService;
    private final TopicService topicService;
    private final CategoryCreateValidator categoryCreateValidator;

    @Autowired
    public ForumController(CategoryService categoryService,
                           TopicService topicService,
                           CategoryCreateValidator categoryCreateValidator) {
        this.categoryService = categoryService;
        this.topicService = topicService;
        this.categoryCreateValidator = categoryCreateValidator;
    }

    @InitBinder(CategoryCreateForm.NAME)
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(categoryCreateValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String forum() {
        return "redirect:/forum/cat";
    }

    @RequestMapping(value="/cat", method = RequestMethod.GET)
    public String showCategory(
            Model model,
            @RequestParam Optional<Long> catId) {
        Category category = catId.
                flatMap(categoryService::findCategoryById).
                orElseGet(categoryService::getRootCategory);
        model.addAttribute("category", category);
        model.addAttribute(CategoryCreateForm.NAME, new CategoryCreateForm());
        model.addAttribute(TopicCreateForm.NAME, new TopicCreateForm());
        return "forum";
    }

    @RequestMapping(value = "/categoryCreate", method = RequestMethod.POST)
         public String handleCategoryCreateForm(
            @Valid @ModelAttribute(CategoryCreateForm.NAME) CategoryCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "category.create.failure");
        } else {
            try {
                categoryService.createNewCategory(form);
                FlashMessage.SUCCESS.put(redirectAttributes, "category.create.success");
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "category.create.failure");
            }
        }
        return "redirect:/forum/cat?catId=" + form.getParentId();
    }

    @RequestMapping(value = "/topicCreate", method = RequestMethod.POST)
    public String handleTopicCreateForm(
            @Valid @ModelAttribute(TopicCreateForm.NAME) TopicCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "topic.create.failure");
        } else {
            try {
                Topic topic = topicService.createNewTopic(form);
                FlashMessage.SUCCESS.put(redirectAttributes, "topic.create.success");
                return "redirect:/forum/topic?topicId=" + topic.getId();
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "topic.create.failure");
            }
        }
        return "redirect:/forum/cat?catId=" + form.getCategoryId();
    }

}
