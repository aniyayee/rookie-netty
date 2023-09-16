package com.rookie.message;

/**
 * @author yayee
 */
public class PingMessage extends Message {

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
