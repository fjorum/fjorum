package org.fjorum.controllers.filters;


import ninja.Filter;
import org.fjorum.models.User;

public class LoggedInFilter extends AbstractUserFilter implements Filter {

    public LoggedInFilter() {
        super(User::isActive, "security.notLoggedIn");
    }
}
