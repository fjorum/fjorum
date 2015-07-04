package org.fjorum.controller;

import org.fjorum.controller.form.CategoryCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private final CategoryService categoryService;

    @Autowired
    public ForumController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String forum(
            Model model,
            @RequestParam Optional<Long> catId) {
        Category category = catId.
                flatMap(categoryService::findCategoryById).
                orElseGet(categoryService::getRootCategory);
        model.addAttribute("category", category);
        model.addAttribute(CategoryCreateForm.NAME, new CategoryCreateForm());
        return "forum";
    }

    @RequestMapping(value = "/categoryCreate", method = RequestMethod.POST)
    public String handleNewCategoryForm(
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
                //bindingResult.reject("email.exists", "Email already exists");
                FlashMessage.ERROR.put(redirectAttributes, "category.create.failure");
            }
        }
        return "redirect:/forum?catId=" + form.getParentId();
    }

    /*
    @RequestMapping(value = "/userCreate", method = RequestMethod.POST)
    public String handleUserCreateForm(
            @Valid @ModelAttribute("userCreateForm") UserCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "user.create.failure");
        } else {
            try {
                userService.create(form);
                FlashMessage.SUCCESS.put(redirectAttributes, "user.create.success");
            } catch (DataIntegrityViolationException e) {
                bindingResult.reject("email.exists", "Email already exists");
                FlashMessage.ERROR.put(redirectAttributes, "user.create.failure");
            }
        }
        return "redirect:/forum?cat=";
    } */
}
