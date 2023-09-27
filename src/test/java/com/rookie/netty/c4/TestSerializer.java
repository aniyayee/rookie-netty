package com.rookie.netty.c4;

import com.rookie.config.Config;
import com.rookie.message.chat.LoginRequestMessage;
import com.rookie.message.Message;
import com.rookie.protocol.MessageCodecSharable;
import com.rookie.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author yayee
 */
public class TestSerializer {

    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);
        LoginRequestMessage message = new LoginRequestMessage("zhang", "123", "zc");
        channel.writeOutbound(message);
        //ByteBuf buf = messageToByteBuf(message);
        //channel.writeInbound(buf);
    }

    public static ByteBuf messageToByteBuf(Message msg) {
        Serializer.Algorithm algorithm = Config.getSerializerAlgorithm();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm.ordinal());
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = algorithm.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
