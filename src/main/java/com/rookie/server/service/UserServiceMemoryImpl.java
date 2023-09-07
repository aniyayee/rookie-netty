package com.rookie.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yayee
 */
public class UserServiceMemoryImpl implements UserService {

    private final Map<String, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put("zhangsan", "123456");
        allUserMap.put("lisi", "123456");
        allUserMap.put("wangwu", "123456");
        allUserMap.put("zhaoliu", "123456");
        allUserMap.put("qianqi", "123456");
    }

    @Override
    public boolean login(String username, String password) {
        String pass = allUserMap.get(username);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
