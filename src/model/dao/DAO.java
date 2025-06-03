package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO <T> {
    private static String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost/AppSecouristes";
    private static String username = "client";
    private static String password = "root";
    protected Connection getConnection () throws SQLException {
        // Charger la classe du pilote
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace ();
            return null;
        }
        // Obtenir la connection
        return DriverManager.getConnection(url , username , password);
    }
    public abstract List <T> findAll ();
    public abstract T findByID(Object... keys);
    public abstract int update(T element);
    public abstract int delete(T element);
    public abstract int create(T element);
}
