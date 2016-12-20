package supermed.usermanagementsystem;

import supermed.datamanagementsystem.DataManager;
import supermed.usermanagementsystem.user.User;

/**
 * Created by Alexander on 24.11.2016.
 */
public class UserManager {

    DataManager dataManager = new DataManager();

    public String createUser(User user, String password) {
        if (user != null && isValidStringParam(password)) {
            return dataManager.createUser(user, password);
        }
        return "";
    }

    public User logIn(String login, String password) {

        if (isValidStringParam(login) && isValidStringParam(password)) {
            return dataManager.logIn(login, password);
        }
        return null;
    }

    public User getUserById(String id) {
        if (isValidStringParam(id)) {
            return dataManager.getUserById(id);
        }
        return null;
    }

    public void updateInfoAboutYourself(String id, String password, String address, String
            contact_phone) {
        dataManager.updateInfoAboutYourself(id, password, address, contact_phone);
    }

    private static boolean isValidStringParam(String param) {
        return param != null && (!param.equals(""));
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
