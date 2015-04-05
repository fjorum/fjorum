package org.fjorum.controllers;

import ninja.Result;
import ninja.Results;
import org.fjorum.annotation.Get;

import javax.inject.Singleton;

@Singleton
public class ErrorController {

    @Get("/errors")
    public Result errors() {
        return Results.html();
    }
}
