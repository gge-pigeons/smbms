package com.zj.servlet.user;

import com.zj.pojo.User;
import com.zj.service.user.UserService;
import com.zj.service.user.UserServiceImp;
import com.zj.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Servlet  控制层调用业务层代码
        System.out.println("LoginServlet---start...");
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //和数据库中的密码进行对比
        UserService userService = new UserServiceImp();
        User user = userService.login(userCode,userPassword);
        if(user!=null){
            System.out.println("登陆成功");
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            resp.sendRedirect("jsp/frame.jsp");
            //resp没有setAttribute
        }
        else{
            //转发回登录页面，顺带提示他用户名和密码错误
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
