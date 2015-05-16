package org.fjorum.controller;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.service.RoleService;
import org.fjorum.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String getAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userCreateForm", new UserCreateForm());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

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
        return "redirect:/admin";
    }
}
