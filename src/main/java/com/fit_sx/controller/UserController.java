package com.fit_sx.controller;

import com.fit_sx.model.User;
import com.fit_sx.service.UserService;
import com.fit_sx.service.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name="userPage",value = "/user/userPage")
public class UserController extends BaseController{
    private UserService userService;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService = new UserServiceImp();
        Integer pageSize = Integer.parseInt(req.getParameter("pageSize"));
        Integer pageNum = Integer.parseInt(req.getParameter("pageNum"));
        resp.setContentType("application/json;charset=utf-8");
        Map map = userService.UserPage(pageSize,pageNum);
        System.out.println(map);
        success(resp,"查询成功！",map);
    }
}
