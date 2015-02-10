package com.fqz.service;

import com.fqz.util.RedisUtil;
import org.springframework.stereotype.Service;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/7
 * Time: 10:24
 */
@Service
public class RedisService {

    public void addValue(String key,String value) throws Exception {
        RedisUtil.set(RedisUtil.Day.MONDAY,key,value);
    }
}
