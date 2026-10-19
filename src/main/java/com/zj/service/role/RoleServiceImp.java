package com.zj.service.role;

import com.zj.dao.BaseDao;
import com.zj.dao.role.RoleDao;
import com.zj.dao.role.RoleDaoImp;
import com.zj.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImp implements RoleService{
    //引入Dao方便直接使用
    private RoleDao roleDao;
    public RoleServiceImp(){
        this.roleDao = new RoleDaoImp();
    }
    public List<Role> getRoleList(){
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }
}
