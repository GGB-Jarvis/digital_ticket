package com.num.digital_ticket.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.num.digital_ticket.entity.RedefinedUserDetails;
import com.num.digital_ticket.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * json序列化工具
 */
public class JsonUtil {
    /**
     * redis user details反序列化
     * @param str
     * @return
     * @throws NullPointerException
     */
    public static UserDetails JsonConvertUserDetails(String str)  throws NullPointerException{

        JSONObject jsonObject = JSON.parseObject(str);
        User user = jsonObject.getObject("user", User.class);
        System.out.println(user);
        JSONArray permissions = jsonObject.getJSONArray("permissions");
        Object[] objects = permissions.stream().toArray();
        List<String> permission = new ArrayList<>();
        for (Object o :
                objects) {
            permission.add((String) o);
        }
        return new RedefinedUserDetails(user, permission);

    }
}
