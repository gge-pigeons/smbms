package com.zj.service.user;

import com.zj.dao.BaseDao;
import com.zj.dao.User.UserDao;
import com.zj.dao.User.UserDaoImp;
import com.zj.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImp implements UserService{
    private UserDao userDao;
    public UserServiceImp(){
        userDao = new UserDaoImp();
    }

    public User login(String userCode, String password){
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user=userDao.getLoginUser(connection,userCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally{
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    public boolean updatePwd(int id,String password){
        Connection connection = null;
        boolean flag=false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,password)>0){
                flag=true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    public int getUserCount(String username,int userRole){
        Connection connection=null;
        int count=0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection,username,userRole);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            BaseDao.closeResource(connection,null,null);
        }
        return count;
    }


    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize){
        Connection connection=null;
        List<User> list =null;
        try {
            connection = BaseDao.getConnection();
            list = userDao.getUserList(connection,queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            BaseDao.closeResource(connection,null,null);
        }
        return list;
    }



}
