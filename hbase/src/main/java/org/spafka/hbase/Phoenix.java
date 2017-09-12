package org.spafka.hbase;

import java.sql.*;

public class Phoenix {

    private static String driver = "org.apache.phoenix.jdbc.PhoenixDriver";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rset = null;

        Connection con = DriverManager.getConnection("jdbc:phoenix:cdh01.spafka.com:2181");
        stmt = con.createStatement();
        rset = stmt.executeQuery("SELECT * from person ");
        while (rset.next()) {
            System.out.println(rset.getString("id"));
            System.out.println(rset.getString("name"));
        }

        stmt.execute("up");
        stmt.close();
        con.close();
    }
}
