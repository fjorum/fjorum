package org.fjorum.controllers.filters;


import ninja.*;
import ninja.jpa.UnitOfWork;
import ninja.session.FlashScope;
import ninja.session.Session;
import org.fjorum.models.User;
import org.fjorum.services.UserService;

import javax.inject.Inject;
import java.util.Optional;

public class LoggedInFilter implements Filter {

    @Inject
    private UserService userService;

    @Override
    @UnitOfWork
    public Result filter(FilterChain filterChain, Context context) {

        Optional<Session> session = Optional.ofNullable(context.getSession());
        Optional<FlashScope> flashScope = Optional.ofNullable(context.getFlashScope());

        return session.
                flatMap(userService::findUserBySession).
                filter(User::isActive).
                map(user -> filterChain.next(context)
                ).orElseGet(() -> {
            flashScope.ifPresent(f -> f.error("security.notLoggedIn"));
            return Results.redirect("/errors");
        });
    }
}
