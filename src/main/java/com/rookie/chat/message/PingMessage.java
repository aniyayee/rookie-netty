package com.rookie.chat.message;

import com.rookie.message.Message;

/**
 * @author yayee
 */
public class PingMessage extends Message {

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
