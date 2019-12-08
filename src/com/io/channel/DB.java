package com.io.channel;

import java.sql.*;

public class DB {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection("");
        Statement st = con.createStatement();
        String userName = "a";
        String password = "b";
        String sql = "select username, password from login where username= '"+userName+"' and password = '"+password+"'";
        System.out.println(sql);
        ResultSet rs = st.executeQuery(sql);
        if(rs.next()){

        }else {

        }
    }
}
