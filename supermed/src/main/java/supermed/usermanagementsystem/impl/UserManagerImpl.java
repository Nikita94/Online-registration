package supermed.usermanagementsystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import supermed.datamanagementsystem.DataManager;
import supermed.datamanagementsystem.impl.DataManagerImpl;
import supermed.usermanagementsystem.UserManager;
import supermed.usermanagementsystem.user.User;

/**
 * Created by Alexander on 24.11.2016.
 */
@Component
public class UserManagerImpl implements UserManager {

    @Autowired
    DataManager dataManager;

    @Override
    public String createUser(User user, String password) {
        if (user != null && isValidStringParam(password)) {
            return dataManager.createUser(user, password);
        }
        return "";
    }

    @Override
    public User logIn(String login, String password) {

        if (isValidStringParam(login) && isValidStringParam(password)) {
            return dataManager.logIn(login, password);
        }
        return null;
    }

    @Override
    public User getUserById(String id) {
        if (isValidStringParam(id)) {
            return dataManager.getUserById(id);
        }
        return null;
    }

    @Override
    public void updateInfoAboutYourself(String id, String password, String address, String
            contact_phone) {
        dataManager.updateInfoAboutYourself(id, password, address, contact_phone);
    }

    private boolean isValidStringParam(String param) {
        return param != null && (!param.equals(""));
    }

}
