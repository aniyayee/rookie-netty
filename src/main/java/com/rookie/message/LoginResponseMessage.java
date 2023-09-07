package com.rookie.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yayee
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
