package supermed.sqlmanagementsystem;

import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import java.sql.*;
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

    private SqlManager() {}

    private static void openConnection() {
        // opening database connection to MySQL server
        try {
            connection = DriverManager.getConnection(url, user, password);
            // getting Statement object to execute query
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try { connection.close(); } catch(SQLException se) { /*can't do anything */ }
        try { statement.close(); } catch(SQLException se) { /*can't do anything */ }
    }

    public static List<User> getUsers() throws SQLException {
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
        finally {
            resultSet.close();
        }
        closeConnection();
        return users;
    }
}
