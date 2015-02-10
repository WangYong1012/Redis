package com.fqz.controller;

import com.fqz.exception.RedisException;
import com.fqz.service.RedisService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/7
 * Time: 10:23
 */
@Controller
public class RedisController {
    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/redis/test", method = RequestMethod.GET)
    public Object getTest() throws Exception {
        redisService.addValue("1","good");
        String s = "geod llasdf";
        return s;
    }
}
