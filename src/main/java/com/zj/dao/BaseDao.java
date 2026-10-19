package com.zj.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    //静态代码块
    static{
        Properties properties = new Properties();
        //通过类加载器读取对应的资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        //驱动只用加载一次
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //获取数据库的连接对象
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection,PreparedStatement preparedStatement, ResultSet resultSet,String sql,Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for(int i=0;i< params.length;i++){
            preparedStatement.setObject(i+1,params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    //编写增删改公共方法
    public static int execute(Connection connection, PreparedStatement preparedStatement, String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for(int i=0;i< params.length;i++){
            preparedStatement.setObject(i+1,params[i]);
        }
        int updateRows=preparedStatement.executeUpdate();
        return updateRows;
    }

    //释放资源
    public static boolean closeResource(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag=true;
        if(resultSet!=null){
            try {
                resultSet.close();
                //GC回收：如果resultSet为空，垃圾回收器就会自动给其清理掉
                resultSet = null;
            } catch (SQLException e) {
                flag=false;
                throw new RuntimeException(e);
            }
        }
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                //GC回收：如果resultSet为空，垃圾回收器就会自动给其清理掉
                preparedStatement = null;
            } catch (SQLException e) {
                flag=false;
                throw new RuntimeException(e);
            }
        }
        if(connection!=null){
            try {
                connection.close();
                //GC回收：如果resultSet为空，垃圾回收器就会自动给其清理掉
                connection = null;
            } catch (SQLException e) {
                flag=false;
                throw new RuntimeException(e);
            }
        }
        return flag;
    }

}
