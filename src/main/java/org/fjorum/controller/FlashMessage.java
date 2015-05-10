package org.fjorum.controller;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

public enum FlashMessage {
    ERROR, SUCCESS, WARNING;

    public void put(RedirectAttributes redirectAttributes, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(this.name().toLowerCase(), value);
        redirectAttributes.addFlashAttribute("flash", map);
    }

    public void put(Model model, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(this.name().toLowerCase(), value);
        model.addAttribute("flash", map);
    }

}
