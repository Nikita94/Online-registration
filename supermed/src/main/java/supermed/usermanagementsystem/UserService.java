package supermed.usermanagementsystem;

import supermed.httpexception.ResourceNotFoundException;
import supermed.usermanagementsystem.user.User;

import javax.naming.NamingException;

/**
 * Created by nikita on 08.12.2016.
 */
public interface UserService {
    User logIn(String login, String password) throws ResourceNotFoundException, NamingException;
}

