package org.fjorum.controllers;

import ninja.Result;
import ninja.Results;
import org.fjorum.controllers.annotations.Get;

import javax.inject.Singleton;

@Singleton
public class ProfileController {

    @Get("/profile")
    public Result profile() {
        return Results.html();
    }

}
