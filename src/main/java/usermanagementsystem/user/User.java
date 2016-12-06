package usermanagementsystem.user;

/**
 * Created by Alexander on 22.11.2016.
 */
public class User {
    final private Role role;
    final private UserData userData;

    public User(UserData userData, Role role) {
        this.userData = userData;
        this.role = role;
    }

}
