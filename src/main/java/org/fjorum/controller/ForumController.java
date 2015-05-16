package org.fjorum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    @RequestMapping(method = RequestMethod.GET)
    public String forum() {
        return "forum";
    }
}
