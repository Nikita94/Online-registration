package supermed.usermanagementsystem.user;

/**
 * Created by Alexander on 11.12.2016.
 */
public class Employee extends User {
    private String hireDate;
    private String position;

    Employee(User user, String hireDate, String position) {
        super(user);
        this.hireDate = hireDate;
        this.position = position;
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getPosition() {
        return position;
    }
}
