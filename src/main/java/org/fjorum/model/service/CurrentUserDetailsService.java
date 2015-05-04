package org.fjorum.model.service;

import org.fjorum.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CurrentUser loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
        User user = userService.getUserByNameOrEmail(nameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(
                                "User with name or email '%s' was not found", nameOrEmail)));
        return new CurrentUser(user);
    }
}