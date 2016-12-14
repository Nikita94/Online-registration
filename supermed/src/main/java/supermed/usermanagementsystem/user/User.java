package supermed.usermanagementsystem.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Alexander on 22.11.2016.
 */

@XmlRootElement
public class User {
    @JsonProperty("user_role")
    private Role role;
    @JsonProperty("user_data")
    private UserData userData;
    @JsonProperty("user_id")
    private String userID;

    public User(String userID, UserData userData, Role role) {
        this.userID = userID;
        this.userData = userData;
        this.role = role;
    }

    public User(UserData userData, Role role) {
        this.userID = null;
        this.userData = userData;
        this.role = role;
    }

    public User(User user) {
        this.userData = user.getUserData();
        this.role = user.getRole();
    }

    public User() {
    }

    public Role getRole() {
        return this.role;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void setID(String userID) {
        this.userID = userID;
    }

    public String getID() {
        return this.userID;
    }
}
