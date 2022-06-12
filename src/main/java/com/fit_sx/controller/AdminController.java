package com.fit_sx.controller;

import com.alibaba.fastjson.JSONObject;
import com.fit_sx.model.Admin;
import com.fit_sx.model.User;
import com.fit_sx.service.AdminService;
import com.fit_sx.util.JsonResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Admin", value = "/Admin")
public class AdminController extends BaseController{
    private AdminService adminService;
    private JsonResult jsonResult;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String method = req.getParameter("method");
        if (method.equals("Login")){

            Admin admin = getBean(Admin.class,req);
            Login(resp,admin);
        }
    }

    private void Login(HttpServletResponse resp,Admin admin) {
        jsonResult = adminService.Login(admin);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", jsonResult.getMsg());
        jsonObject.put("code", jsonResult.getCode());
        try {
            resp.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
