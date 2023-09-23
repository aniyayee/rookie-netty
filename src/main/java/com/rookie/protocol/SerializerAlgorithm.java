package com.rookie.protocol;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author yayee
 */
public enum SerializerAlgorithm implements Serializer {
    Java {
        @Override
        public <T> byte[] serialize(T object) {
            byte[] bytes = null;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(object);
                bytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("SerializerAlgorithm.Java 序列化错误", e);
            }
            return bytes;
        }

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            T message = null;
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
                message = (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("SerializerAlgorithm.Java 反序列化错误", e);
            }
            return message;
        }
    },

    Json {
        @Override
        public <T> byte[] serialize(T object) {
            String s = new Gson().toJson(object);
            return s.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            String s = new String(bytes, StandardCharsets.UTF_8);
            return new Gson().fromJson(s, clazz);
        }
    };

    // 从协议的字节中得到是哪种序列化算法
    public static SerializerAlgorithm getByInt(int type) {
        SerializerAlgorithm[] array = SerializerAlgorithm.values();
        if (type < 0 || type > array.length - 1) {
            throw new IllegalArgumentException("超过 SerializerAlgorithm 范围");
        }
        return array[type];
    }
}
