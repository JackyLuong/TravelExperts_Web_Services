package com.example.travelexperts_web_services.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionMngr {
    //DB connection instance
    private static DBConnectionMngr instance = null;

    //set default constructor to private to restrict object creation. Object creation Only via the static method.
    private DBConnectionMngr(){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBConnectionMngr getInstance(){
        if(instance == null){
            instance = new DBConnectionMngr();
        }return instance;
    }

    public Connection getConnection(){
        Connection conn = null;
        try {

            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/travelexpertsworkshop6", "jacky", "password");
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        }

        return conn;
    }

}