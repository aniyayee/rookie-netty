package com.rookie.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 响应消息中只需要获取返回结果和异常值
 *
 * @author yayee
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends AbstractResponseMessage {

    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
