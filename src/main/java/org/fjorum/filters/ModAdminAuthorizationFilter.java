package org.fjorum.filters;


import com.google.inject.Inject;
import ninja.*;
import ninja.jpa.UnitOfWork;
import ninja.session.FlashScope;
import ninja.session.Session;
import org.fjorum.models.User;
import org.fjorum.services.UserService;

import java.util.Optional;

public class ModAdminAuthorizationFilter implements Filter {

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
                filter(user -> user.isAdministrator() || user.isModerator()).
                map(user -> filterChain.next(context)
        ).orElseGet(() -> {
            flashScope.ifPresent(f -> f.error("security.noAdminAuthorization"));
            return Results.redirect("/errors");
        });
    }
}
