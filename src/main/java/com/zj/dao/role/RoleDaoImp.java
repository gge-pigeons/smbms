package com.zj.dao.role;

import com.zj.dao.BaseDao;
import com.zj.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImp implements RoleDao{
    public List<Role> getRoleList(Connection connection) throws SQLException {
        List<Role> list = new ArrayList<>();
        PreparedStatement pstm=null;
        ResultSet rs=null;
        if(connection!=null){
            String sql = "select * from smbms_role";
            Object[] params={};
            ResultSet resultSet = BaseDao.execute(connection,pstm,rs,sql,params);
            while(resultSet.next()){
                Role _role = new Role();
                _role.setId(resultSet.getInt("id"));
                _role.setRoleCode(resultSet.getString("roleCode"));
                _role.setRoleName(resultSet.getString("roleName"));
                list.add(_role);
            }
        }
        BaseDao.closeResource(connection,null,null);
        return list;
    }
}
