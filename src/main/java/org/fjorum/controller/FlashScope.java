package org.fjorum.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

public interface FlashScope {

    static void error(RedirectAttributes redirectAttributes, String error) {
        flashScope(redirectAttributes, "error", error);
    }

    static void success(RedirectAttributes redirectAttributes, String success) {
        flashScope(redirectAttributes, "success", success);
    }

    static void warning(RedirectAttributes redirectAttributes, String warning) {
        flashScope(redirectAttributes, "warning", warning);
    }

    static void flashScope(RedirectAttributes redirectAttributes, String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        redirectAttributes.addFlashAttribute("flash", map);
    }

}
