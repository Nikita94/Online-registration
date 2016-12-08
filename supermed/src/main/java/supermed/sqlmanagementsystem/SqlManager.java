package supermed.sqlmanagementsystem;


import java.sql.*;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nikita on 08.12.2016.
 */
public final class SqlManager {
    private static final String url = "jdbc:mysql://localhost:3306/supermed";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static InitialContext initialContext;
    private static DataSource dataSource;

    private SqlManager() {}

    private static void openConnection() {
        // opening database connection to MySQL server
        try {
            initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/supermed");
            connection = dataSource.getConnection();
            // getting Statement object to execute query
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection() throws SQLException, NamingException {
        if (resultSet != null)
            resultSet.close();
        if (statement != null)
            statement.close();
        if (connection != null)
            connection.close();
        if (initialContext != null)
            initialContext.close();
    }

    public static List<User> getUsers() throws SQLException, NamingException {
        openConnection();
        List<User> users = new ArrayList<User>();
        try {
            resultSet = statement.executeQuery("select * from users");
            while (resultSet.next()) {
                User user = new User();
                Role role = Role.createRole(resultSet.getString("role"));
                UserData userData = UserData.newBuilder()
                        .setFirstName(resultSet.getString("name"))
                        .setMiddleName(resultSet.getString("s_name"))
                        .setLastName(resultSet.getString("patronymic"))
                        .setLogin(resultSet.getString("login"))
                        .setPassportData(resultSet.getString("password"))
                        .build();

                user.setRole(role);
                user.setUserData(userData);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return users;
    }

    public static void main(String[] args) throws NamingException {
        try {
            System.out.print(getUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
