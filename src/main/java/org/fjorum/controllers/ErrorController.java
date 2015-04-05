package org.fjorum.controllers;

import com.google.inject.Singleton;
import ninja.Result;
import ninja.Results;
import org.fjorum.annotation.Get;

@Singleton
public class ErrorController {

    @Get("/errors")
    public Result errors() {
        return Results.html();
    }
}
