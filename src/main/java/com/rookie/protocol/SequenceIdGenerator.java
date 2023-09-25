package com.rookie.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yayee
 */
public abstract class SequenceIdGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
