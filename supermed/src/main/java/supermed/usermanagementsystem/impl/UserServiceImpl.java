package supermed.usermanagementsystem.impl;

import supermed.datamanagementsystem.DataManager;
import supermed.usermanagementsystem.UserService;
import supermed.usermanagementsystem.user.User;

import javax.naming.NamingException;
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
            users = DataManager.getUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User goalUser = null;
        for (User user : users) {
            if (user.getUserData().getLogin().equals(login)) {
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
