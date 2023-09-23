package com.rookie.server.service;

/**
 * @author yayee
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        //int i = 1 / 0;
        return "hey, " + msg;
    }
}
