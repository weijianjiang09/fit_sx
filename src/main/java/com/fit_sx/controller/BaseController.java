package com.fit_sx.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

public class BaseController extends HttpServlet {
    /**
     * 反射获取bean
     * @param cl
     * @param req
     * @return
     */
    protected <T> T  getBean(Class cl, HttpServletRequest req) {
        T t = null;
        try {
            t = (T) cl.newInstance();
        }catch (InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        Field[] fields= cl.getDeclaredFields();
        for (Field field : fields) {
            if(req.getParameter(field.getName())!=null){
                try {
                    field.setAccessible(true);//开启权限
                    if(field.getType().equals(Long.class)) {
                        field.set(t, Long.valueOf(req.getParameter(field.getName())));
                    }else if(field.getType().equals(Integer.class)) {
                        field.set(t, Integer.valueOf(req.getParameter(field.getName())));
                    }else if(field.getType().equals(String.class)) {
                        field.set(t, req.getParameter(field.getName()));
                    }else if(field.getType().equals(BigDecimal.class)) {
                        field.set(t, new BigDecimal(req.getParameter(field.getName())));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
    protected void success(HttpServletResponse resp, String msg, Object obj) throws IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("code", 1);
        jsonObject.put("data", obj);
        resp.getWriter().write(jsonObject.toString());
    }

    protected void success(HttpServletResponse resp,String msg) throws IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("code", 1);
        resp.getWriter().write(jsonObject.toString());
    }

    protected void fail(HttpServletResponse resp,String msg) throws IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("code", 2);
        resp.getWriter().write(jsonObject.toString());
    }
}
