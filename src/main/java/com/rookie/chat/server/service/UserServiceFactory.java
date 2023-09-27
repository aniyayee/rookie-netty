package com.rookie.chat.server.service;

/**
 * @author yayee
 */
public abstract class UserServiceFactory {

    private static final UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
