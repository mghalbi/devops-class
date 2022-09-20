package org.sorint.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Driver;

public class Main {

    public static void main(String[] args) throws Exception {
        DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance());

        String url = "jdbc:mysql://localhost:3306?user=root&password=root";

        try  {
            Connection con= DriverManager.getConnection(url);
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT 1");
            while (rs.next()){
                System.out.println("Result: " + rs.getInt(1));
            }
        } catch (Exception e ){
            throw  new Exception();
        }
    }
}
