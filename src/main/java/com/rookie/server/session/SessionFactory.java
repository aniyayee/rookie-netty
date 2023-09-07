package com.rookie.server.session;

/**
 * @author yayee
 */
public class SessionFactory {

    private static final Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
