package supermed.usermanagementsystem.user;

import com.fasterxml.jackson.databind.util.Named;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Alexander on 23.11.2016.
 */
@Document
public enum Role implements Named{
    DOCTOR("doctor"),
    MANAGER("manager"),
    PATIENT("patient");

    String role;

    Role(String role) {
        this.role = role;
    }

    public String getName() {
        return role;
    }

    public static Role createRole(String role) {
        if (role != null) {
            for (Role resource : Role.values()) {
                if (role.equalsIgnoreCase(resource.role)) {
                    return resource;
                }
            }
        }
        return null;
    }
}
