package supermed.usermanagementsystem.impl;

import supermed.httpexception.ResourceNotFoundException;
import supermed.sqlmanagementsystem.SqlManager;
import supermed.usermanagementsystem.UserService;
import supermed.usermanagementsystem.user.User;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 08.12.2016.
 */
public class UserServiceImpl implements UserService {

    @Override
    public User logIn(String login, String password) throws NamingException {
        List<User> users = new ArrayList<User>();
        try {
            users = SqlManager.getUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User goalUser = null;
        for (User user : users) {
            if (user.getUserData().getLogin().equals(login)
                    && user.getUserData().getPassportData().equals(password)) {
                goalUser = user;
                break;
            }
        }
        return goalUser;
    }

    //public static void main(String[] args) {
    //    UserServiceImpl userService = new UserServiceImpl();
    //    userService.logIn("peiv", "pass");
    //}
}
