package supermed.datamanagementsystem;


import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by nikita on 08.12.2016.
 */
public final class DataManager {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static InitialContext initialContext;
    private static DataSource dataSource;

    private DataManager() {
    }

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


    public static User getUserByLogin(String login) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from users where login = '" + login + "'");
            return constructUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static User logIn(String login, String password) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from users where login = '" + login +
                    "' and password = '" + password + "'");
            return constructUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static User getUserById(String id) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from users where id = " + id);
            return constructUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static User constructUser() {
        User user = new User();
        try {
            if (resultSet.next()) {
                Role role = Role.createRole(resultSet.getString("role"));
                UserData userData = UserData.newBuilder()
                        .setFirstName(resultSet.getString("first_name"))
                        .setMiddleName(resultSet.getString("middle_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setLogin(resultSet.getString("login"))
                        .setBirthDate(resultSet.getString("birth_date"))
                        .setAddress(resultSet.getString("address"))
                        .setPhoneNumber(resultSet.getString("contact_phone"))
                        .build();
                user.setRole(role);
                user.setUserData(userData);
                user.setID(resultSet.getString("id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createUser(User user, String password) {
        UserData userData = user.getUserData();
        String query = "INSERT INTO `users` VALUES (NULL,'" +
                userData.getLogin() +"','"+
                password + "','" +
                userData.getFirstName() + "','" +
                userData.getMiddleName() + "','" +
                userData.getLastName() + "','" +
                userData.getBirthDate() + "','" +
                userData.getAddress() +"','" +
                userData.getPhoneNumber() + "','" +
                user.getRole().getName() + "');";
        return executeUpdateQuery(query);
    }

    public static boolean updateInfoAboutYourself(String id, String password, String address, String contact_phone) {
        String query = "UPDATE users SET password = \"" +
                password + "\", address = \"" +
                address + "\", contact_phone = \"" +
                contact_phone + "\" WHERE id = " + id;
        return executeUpdateQuery(query);
    }

    private static boolean executeUpdateQuery(String query) {
        openConnection();
        try {
            if (statement.executeUpdate(query) != 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
