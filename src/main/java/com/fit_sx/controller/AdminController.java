package com.fit_sx.controller;

import com.alibaba.fastjson.JSONObject;
import com.fit_sx.model.Admin;
import com.fit_sx.service.AdminService;
import com.fit_sx.service.AdminServiceImpl;
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

        String method = req.getParameter("method");
        if (method.equals("Login")){
            Admin admin = getBean(Admin.class,req);
            this.Login(resp,admin);
        }else if (method.equals("Add")){
            Admin admin = getBean(Admin.class,req);
            Add(resp,admin);

        }else if (method.equals("List")){
            Integer pageNum ;
            if (req.getParameter("pageNum")==null){
                pageNum=1;
            }else pageNum=Integer.valueOf(req.getParameter("pageNum"));
            Integer pageSize ;
            if (req.getParameter("pageSize")==null){
                pageSize=5;
            }else pageSize=Integer.valueOf(req.getParameter("pageSize"));
            AdminPage(resp,pageNum,pageSize);
        }
    }

    //注册
    private void Add(HttpServletResponse resp, Admin admin) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        adminService = new AdminServiceImpl();
        jsonResult = adminService.Add(admin);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", jsonResult.getMsg());
        jsonObject.put("code", jsonResult.getCode());
        jsonObject.put("data",jsonResult.getData());
        resp.getWriter().write(jsonObject.toString());
    }

    //列表
    private void AdminPage(HttpServletResponse resp, Integer pageNum, Integer pageSize) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        adminService = new AdminServiceImpl();
        jsonResult = adminService.AdminPage(pageSize,pageNum);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("data",jsonResult.getData());
        resp.getWriter().write(jsonObject.toString());
    }


    //登录
    private void Login(HttpServletResponse resp,Admin admin) throws IOException {

        resp.setContentType("application/json;charset=utf-8");
        adminService = new AdminServiceImpl();
        jsonResult = adminService.Login(admin);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", jsonResult.getMsg());
        jsonObject.put("code", jsonResult.getCode());

            resp.getWriter().write(jsonObject.toString());

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
