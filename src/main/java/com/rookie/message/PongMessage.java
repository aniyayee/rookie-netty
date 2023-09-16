package com.rookie.message;

/**
 * @author yayee
 */
public class PongMessage extends Message {

    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
