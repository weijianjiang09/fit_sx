package com.fit_sx.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fit_sx.common.FileResult;
import com.fit_sx.common.ReadConfig;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class BaseController extends HttpServlet {
    protected <T> T getBeanForMap(Class cl, Map<String, String> map) {

        T t=null;
        try {
            t=(T) cl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Field[] fields= cl.getDeclaredFields();
        for (Field field : fields) {
            if(map.get(field.getName())!=null){
                try {
                    field.setAccessible(true);//开启权限
                    if(field.getType().equals(Long.class)) {
                        field.set(t, Long.valueOf(map.get(field.getName())));
                    }else if(field.getType().equals(Integer.class)) {
                        field.set(t, Integer.valueOf(map.get(field.getName())));
                    }else if(field.getType().equals(String.class)) {
                        field.set(t, map.get(field.getName()));
                    }else if(field.getType().equals(BigDecimal.class)) {
                        field.set(t, new BigDecimal(map.get(field.getName())));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    protected FileResult getFile(HttpServletRequest req) {
        FileResult result=new FileResult();
        String savePath = ReadConfig.read("REALY_PATH");
        File file = new File(savePath);
        if(!file.exists()&&!file.isDirectory()){
            System.out.println("目录或文件不存在！");
            file.mkdirs();
        }
        //消息提示
        try {
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            //解决上传文件名的中文乱码
            fileUpload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if(!fileUpload.isMultipartContent(req)){
                //按照传统方式获取数据
                result.setMsg("无文件，请测试程序！");
                result.setFlag(false);
                return result;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = fileUpload.parseRequest(req);
            for (FileItem item : list) {
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value;
                    try {
                        value = item.getString("UTF-8");
                        result.getParam().put(name, value);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }else{
                    //如果fileitem中封装的是上传文件，得到上传的文件名称，
                    String fileName = item.getName();
                    if(fileName==null||fileName.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    fileName = fileName.substring(fileName.lastIndexOf("."));
                    //获取item中的上传文件的输入流
                    InputStream is;
                    try {
                        is = item.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        result.setMsg("文件异常！");
                        return result;
                    }
                    //创建一个文件输出流
                    FileOutputStream fos;
                    try {
                        fileName=System.currentTimeMillis()+fileName;
                        fos = new FileOutputStream(savePath+"/"+fileName);
                        result.setPath(savePath+"/"+fileName);
                        result.setFileName(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        result.setMsg("文件异常！");
                        return result;
                    }
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int length = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    try {
                        while((length = is.read(buffer))>0){
                            //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                            fos.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        result.setMsg("文件异常！");
                        return result;
                    }
                    //关闭输入流
                    try {
                        is.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //关闭输出流
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    result.setMsg("文件上传成功！");
                    result.setFlag(true);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            result.setMsg("文件上传失败！");
            return result;
        }
        return result;
    }

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
        resp.getWriter().write(JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue));
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
