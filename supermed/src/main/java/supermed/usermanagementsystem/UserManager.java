package supermed.usermanagementsystem;

import supermed.datamanagementsystem.DataManager;
import supermed.usermanagementsystem.user.User;

/**
 * Created by Alexander on 24.11.2016.
 */
public class UserManager {


    public static boolean createUser(User user, String password) {
        if (user != null && isValidStringParam(password)) {
            return DataManager.createUser(user, password);
        }
        return false;
    }

    public static User logIn(String login, String password) {

        if (isValidStringParam(login) && isValidStringParam(password)) {
            return DataManager.logIn(login, password);
        }
        return null;
    }

    public static User getUserById(String id) {
        if (isValidStringParam(id)) {
            return DataManager.getUserById(id);
        }
        return null;
    }

    public static void updateInfoAboutYourself(String id, String password, String address, String
            contact_phone) {
        DataManager.updateInfoAboutYourself(id, password, address, contact_phone);
    }

    private static boolean isValidStringParam(String param) {
        return param != null && (!param.equals(""));
    }
}
