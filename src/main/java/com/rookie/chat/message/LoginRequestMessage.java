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
public class LoginRequestMessage extends Message {

    private String username;
    private String password;
    private String nickname;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
