package com.rookie.chat.message;

import com.rookie.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yayee
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GroupQuitRequestMessage extends Message {

    private String groupName;
    private String username;

    public GroupQuitRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
