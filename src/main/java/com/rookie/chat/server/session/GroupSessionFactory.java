package com.rookie.chat.server.session;

/**
 * @author yayee
 */
public class GroupSessionFactory {

    private static final GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
