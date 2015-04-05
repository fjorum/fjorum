package org.fjorum.services;

public interface AfterUserCreationHook {

    void execute(String email);

}