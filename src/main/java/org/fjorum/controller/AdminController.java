package org.fjorum.controller;

import org.fjorum.controller.form.*;
import org.fjorum.model.entity.User;
import org.fjorum.model.service.RoleService;
import org.fjorum.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_PAGE = "admin";
    private static final String REDIRECT_ADMIN_PAGE = "redirect:/admin";
    private static final String USER_DELETE_FORM_NAME = "userDeleteForm";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final RoleService roleService;
    private final UserCreateValidator userCreateValidator;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService,
                           UserCreateValidator userCreateValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userCreateValidator = userCreateValidator;
    }

    @InitBinder(UserCreateForm.NAME)
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String getAdminPage(Model model) {
        List<UserEditForm> editForms = userService.getAll(new Sort("name")).stream()
                .map(UserEditForm::new)
                .collect(Collectors.toList());

        //model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute(UserCreateForm.NAME, new UserCreateForm());
        model.addAttribute(UserEditForm.NAME + "s", editForms);
        //model.addAttribute(UserRightsForm.NAME, new UserRightsForm());
        //model.addAttribute(USER_DELETE_FORM_NAME, new DeleteForm());
        return ADMIN_PAGE;
    }

    @RequestMapping(value = "/userCreate", method = RequestMethod.POST)
    public String handleUserCreateForm(
            @Valid UserCreateForm userCreateForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "user.create.failure");
        } else {
            try {
                userService.create(userCreateForm);
                FlashMessage.SUCCESS.put(redirectAttributes, "user.create.success");
            } catch (DataIntegrityViolationException e) {
                bindingResult.reject("email.exists", "Email already exists");
                FlashMessage.ERROR.put(redirectAttributes, "user.create.failure");
            }
        }
        return REDIRECT_ADMIN_PAGE;
    }

    @RequestMapping(value = "/userEdit", method = RequestMethod.POST)
    public String handleUserEditForm(
            @Valid UserEditForm userEditForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "user.edit.failure");
        } else {
            try {
                User user = userEditForm.getEditedUser();
                user.setName(userEditForm.getName());
                user.setEmail(userEditForm.getEmail());
                user.setActive(userEditForm.isActive());
                userService.save(user);
                FlashMessage.SUCCESS.put(redirectAttributes, "user.edit.success");
            } catch (DataIntegrityViolationException e) {
                bindingResult.reject("email.exists", "Email already exists");
                FlashMessage.ERROR.put(redirectAttributes, "user.edit.failure");
            }
        }
        return REDIRECT_ADMIN_PAGE;
    }

    @RequestMapping(value = "/userRights", method = RequestMethod.POST)
    public String handleUserRightsForm(
            @Valid UserRightsForm userRightsForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FlashMessage.ERROR.put(redirectAttributes, "user.rights.failure");
        } else {
            try {
                userService.changeRights(userRightsForm);
                FlashMessage.SUCCESS.put(redirectAttributes, "user.rights.success");
            } catch (DataIntegrityViolationException e) {
                FlashMessage.ERROR.put(redirectAttributes, "user.rights.failure");
            }
        }
        return REDIRECT_ADMIN_PAGE;
    }

    @RequestMapping(value = "/userDelete", method = RequestMethod.POST)
    public String handleUserDeleteForm(
            @Valid DeleteForm form,
            RedirectAttributes redirectAttributes) {
        try {
            userService.delete(userService.
                    getById(form.getEntityId()).
                    orElseThrow(NoSuchElementException::new));
            FlashMessage.SUCCESS.put(redirectAttributes, "user.delete.success");
        } catch (RuntimeException e) {
            logger.error("Can't delete user " + form.getEntityId(), e);
            FlashMessage.ERROR.put(redirectAttributes, "user.delete.failure");
        }
        return REDIRECT_ADMIN_PAGE;
    }

}
