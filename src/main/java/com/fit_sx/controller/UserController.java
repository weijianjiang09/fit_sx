package com.fit_sx.controller;

import com.fit_sx.common.FileResult;
import com.fit_sx.model.Admin;
import com.fit_sx.model.User;
import com.fit_sx.service.UserService;
import com.fit_sx.service.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="user",value = "/user")
public class UserController extends BaseController{
    private UserService userService = new UserServiceImp();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String method = req.getParameter("method");
        System.out.println(method);
        if (method.equals("page") ){
            this.UserPage(req, resp);
        }else if(method.equals("create") ){
            this.UserCreate(req, resp);
        }else if(method.equals("login")){
            this.UserLogin(req, resp);
        }else {
            this.User404(req, resp);
        }
    }

    protected void UserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer pageSize = Integer.parseInt(req.getParameter("pageSize"));
        Integer pageNum = Integer.parseInt(req.getParameter("pageNum"));
        String account = req.getParameter("account");
        String name = req.getParameter("name");
        resp.setContentType("application/json;charset=utf-8");
        Map<String,Object> maps =new HashMap<>();
        maps.put("pageSize",pageSize);
        maps.put("pageNum",pageNum);
        maps.put("account",account);
        maps.put("name",name);
        Map map = userService.UserPage(maps);
        System.out.println(map);
        success(resp,"查询成功！",map);
    }

    protected void UserCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        FileResult fileResult = getFile(req);
        if (!fileResult.isFlag()){
            fail(resp,fileResult.getMsg());
            return;
        }
//        User user = getBean(User.class,req);
////        System.out.println(req.getParameter("name"));
        User user = getBeanForMap(User.class,fileResult.getParam());
        if (user.getAccount().length()>12||user.getAccount().length()<4){
            fail(resp,"账号必须是小于12位大于4位的");
        }else if (user.getPassword().length()>12||user.getAccount().length()<6){
            fail(resp,"密码必须是小于12位大于6位的");
        } else if (user.getName().length()>6||user.getAccount().length()<2){
            fail(resp,"密码必须是小于6位大于2位的");
        }
        user.setHeadImg(fileResult.getFileName());
        Boolean flag = userService.UserCreated(user);
        if (flag){
            success(resp,"登录成功");
        }else {
            fail(resp,"创建错误!账号重复");
        }

    }

    protected void UserLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        User user = getBean(User.class,req);
        System.out.println(user);
        User user1 = userService.UserLogin(user);
        if (user1 != null){
            success(resp,"登录成功！",user1);
        }else {
            fail(resp,"登录失败！请检查账号密码是否正确");
        }
    }


    protected void User404(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json;charset=utf-8");
        fail(resp,"404");
    }
}
