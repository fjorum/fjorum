package org.fjorum.controllers;

import ninja.Result;
import ninja.Results;
import org.fjorum.controllers.annotations.Get;

import javax.inject.Singleton;

@Singleton
public class ErrorController {

    @Get("/errors")
    public Result errors() {
        return Results.html();
    }
}
