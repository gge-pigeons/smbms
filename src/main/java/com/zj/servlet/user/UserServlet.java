package com.zj.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.zj.pojo.Role;
import com.zj.pojo.User;
import com.zj.service.role.RoleServiceImp;
import com.zj.service.user.UserService;
import com.zj.service.user.UserServiceImp;
import com.zj.utils.Constants;
import com.zj.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String method = req.getParameter("method");
       if(method.equals("savepwd")&&method!=null){
           this.updatePwd(req,resp);
       }else if(method.equals("pwdmodify")&&method!=null){
           this.pwdModify(req,resp);
       }else if(method.equals("query")&&method!=null){
           this.query(req,resp);
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest req,HttpServletResponse resp){
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole =0;


        //获取用户列表
        UserServiceImp userService = new UserServiceImp();
        List<User> userList = null;
        //第一次走这个请求，一定是第一页，页面大小固定的
        int pageSize=5;
        int currentPageNo=1;
        if(queryUserName==null) {
            queryUserName = "";
        }
        if(temp!=null &&!temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }
        if(pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户的总数
        int totalCount = userService.getUserCount(queryUserName,queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        //控制首页和尾页  获得总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        if(currentPageNo<1){
            currentPageNo=1;
        }
        else if(currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }
        //获取用户列表展示
        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
        req.setAttribute("userList",userList);

        RoleServiceImp roleService =new RoleServiceImp();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //修改密码
    public void updatePwd(HttpServletRequest req,HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag=false;
        if(o!=null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImp();
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                req.setAttribute("message","修改密码成功，请退出使用新密码登录");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.setAttribute("message","密码修改失败");
            }
        }else{
            req.setAttribute("message","新密码有问题要么为空要么为null");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //验证旧密码,session中有旧密码
    public void pwdModify(HttpServletRequest req,HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map结果表
        Map<String,String> resultMap = new HashMap<String,String>();
        if(o==null){//Session过期了
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else{
            String userPassword = ((User)o).getUserPassword();
            if(oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            }else{
                resultMap.put("result","false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
