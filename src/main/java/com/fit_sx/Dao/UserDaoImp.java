package com.fit_sx.Dao;

import com.fit_sx.model.User;
import com.fit_sx.util.SQLUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImp implements UserDao{

    @Override
    public Map UserPage(Integer pageSize, Integer pageNum) {
//        SELECT * FROM () w,() c
        Map<String,Object> map =new HashMap<>();;
        List<User> list =  SQLUtil.find(User.class,"SELECT * FROM user  limit ?, ?;",(pageNum-1)*pageSize,pageSize);

        BigDecimal count =  SQLUtil.getQueryDecimal("select count(*)  from user","count(*)");
//        System.out.println(count);
        map.put("rows",list);
        map.put("count",count);
        return map;
    }
}
