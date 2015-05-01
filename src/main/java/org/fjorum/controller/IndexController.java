package org.fjorum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", "Hello Fjorum!");
        return "index";
    }

    /*@RequestMapping(value = "/", method = RequestMethod.POST)
    public String login(Session session, FlashScope put, Validation validation,
                        @Param("emailOrName") @Required @Length(min = 1) String emailOrName,
                        @Param("password") @Required @Length(min = 1) String passwordPlainText) {

        return (validation.hasViolations()
                ? Optional.<User>empty()
                : userService.findUserByEmailOrName(emailOrName)).

                filter(user -> userService.isPasswordValid(user, passwordPlainText)).
                map(user -> {
                    session.put(UserMessages.USER_ID, user.getId().toString());
                    put.success(UserMessages.USER_LOGIN_FLASH_SUCCESS);
                    return "redirect:/";
                }).
                orElseGet(() -> {
                    put.put("email", emailOrName);
                    put.error(UserMessages.USER_LOGIN_FLASH_ERROR);
                    return "redirect:/";
                });
    } */

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes) {
        FlashMessage.SUCCESS.put(redirectAttributes, "user.logout.flash.success");
        return ("redirect:/");
    }

}
