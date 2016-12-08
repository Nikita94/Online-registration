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

    public User(UserData userData, Role role) {
        this.userData = userData;
        this.role = role;
    }

    public User() {}

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
}
