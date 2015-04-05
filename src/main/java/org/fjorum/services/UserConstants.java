package org.fjorum.services;


public interface UserConstants {

    /* must be set in application conf */
    public static String EMAIL_FROM = "security.emailFrom";

    /* optional in application conf */
    public static String SECURE_URL = "security.secureUrl";

    /* optional in application conf */
    public static String REGULAR_URL = "security.regularUrl";

    /**
     * must implement AfterUserCreationHook interface...
     */
    public static String AFTER_CREATION_HOOK = "security.afterUserCreationHook";

    public static final String USER_MANAGER = "security.userManager";

    public static final String HASH_ROUNDS_KEY = "security.bcyrptHashRounds";

    public static final int HASH_ROUNDS_DEFAULT_VALUE = 10;

    public static final String EMAIL_FROM_ADDRESS = "security.email.from.address";

}
