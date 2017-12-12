package supermed.usermanagementsystem;

import org.springframework.stereotype.Component;
import supermed.httpexception.ResourceNotFoundException;
import supermed.usermanagementsystem.user.User;

import javax.naming.NamingException;

/**
 * Created by nikita on 08.12.2016.
 */

public interface UserManager {
    String createUser(User user, String password);

    User logIn(String login, String password) throws ResourceNotFoundException, NamingException;

    User getUserById(String id);

    void updateInfoAboutYourself(String id, String password, String address, String
            contact_phone);
}

