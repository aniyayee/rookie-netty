package com.rookie.rpc.server.service;

/**
 * @author yayee
 */
public class MathServiceImpl implements MathService {

    @Override
    public String add(Integer a, Integer b) {
        return String.valueOf(a + b);
    }
}
