package com.rookie.chat.server.session;

import java.util.Collections;
import java.util.Set;
import lombok.Data;

/**
 * 聊天组，即聊天室
 *
 * @author yayee
 */
@Data
public class Group {

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());
    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}
