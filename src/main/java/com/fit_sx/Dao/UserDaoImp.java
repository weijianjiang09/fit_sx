package com.fit_sx.Dao;

import com.fit_sx.model.User;
import com.fit_sx.util.MD5Util;
import com.fit_sx.util.SQLUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImp implements UserDao{

    @Override
    public Map UserPage(Map<String,Object> maps) {
        Map<String,Object> map =new HashMap<>();
        List<User> list =null;
        BigDecimal count =null;

        Integer pageNum =(Integer) maps.get("pageNum");
        Integer pageSize =(Integer) maps.get("pageSize");

        if (maps.get("account")==null&&maps.get("name")==null){
            list =  SQLUtil.find(User.class,"SELECT * FROM user order by created_at DESC limit ?, ? ;",(pageNum-1)*pageSize,pageSize);
            count =  SQLUtil.getQueryDecimal("select count(*)  from user","count(*)");
        }else if (maps.get("account")!=null){
            list =  SQLUtil.find(User.class,"SELECT * FROM user where account=? order by created_at DESC limit ?, ? ;",maps.get("account"),(pageNum-1)*pageSize,pageSize);
        }else {
            list =  SQLUtil.find(User.class,"SELECT * FROM user where name like concat('%',?,'%') order by created_at DESC limit ?, ? ;",maps.get("name"),(pageNum-1)*pageSize,pageSize);
        }
        map.put("rows",list);
        map.put("count",count);
        return map;
    }

    @Override
    public boolean UserCrated(User user) {
        User res = SQLUtil.findById(User.class,"user",user.getAccount(),"account");
        if (res!=null){
            return false;
        }
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setCreatedAt(df.format(day));
        user.setPassword(MD5Util.GetMD5Code(user.getPassword())+"12345");
        user.setPassword(MD5Util.GetMD5Code(user.getPassword()));
        return SQLUtil.insert("user",user);
    }

    @Override
    public User UserLogin(User user) {
        User res = SQLUtil.findById(User.class,"user",user.getAccount(),"account");
        String first =  MD5Util.GetMD5Code(user.getPassword());
        String send = MD5Util.GetMD5Code(first+"12345");
        if (res.getPassword().equals(send)){
            return res;
        }
        return null;
    }


}
