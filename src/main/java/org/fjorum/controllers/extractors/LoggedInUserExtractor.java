package org.fjorum.controllers.extractors;

import ninja.Context;
import ninja.jpa.UnitOfWork;
import ninja.params.ArgumentExtractor;
import org.fjorum.models.User;
import org.fjorum.services.UserService;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class LoggedInUserExtractor implements ArgumentExtractor<Optional<User>> {

    //field injection doesn't work here
    private final UserService userService;

    @Inject
    public LoggedInUserExtractor(UserService userService) {
       this.userService = userService;
    }

    @Override
    @UnitOfWork
    public Optional<User> extract(Context context) {
        return ofNullable(context).
                flatMap(c -> ofNullable(c.getSession())).
                flatMap(userService::findUserBySession);
    }

    @Override
    public Class getExtractedType() {
        return Optional.class;
    }

    @Override
    public String getFieldName() {
        return null;
    }
}