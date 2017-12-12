package supermed.usermanagementsystem.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 22.11.2016.
 */

@XmlRootElement
@Document(collection="users")
public class User implements Serializable {
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
        this.userID = user.getID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (role != user.role) return false;
        if (userData != null ? !userData.equals(user.userData) : user.userData != null)
            return false;
        return userID != null ? userID.equals(user.userID) : user.userID == null;

    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (userData != null ? userData.hashCode() : 0);
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
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
