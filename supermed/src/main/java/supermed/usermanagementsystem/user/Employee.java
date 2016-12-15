package supermed.usermanagementsystem.user;

import javax.jws.soap.SOAPBinding;

/**
 * Created by Alexander on 11.12.2016.
 */
public class Employee extends User {
    private String hireDate;
    private String position;
    private String branchAddress;

    private Employee(User user, String hireDate, String position, String branchAddress) {
        super(user);
        this.hireDate = hireDate;
        this.position = position;
        this.branchAddress = branchAddress;
    }

    private Employee() {
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getPosition() {
        return position;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public static Employee.EmployeeBuilder newBuilder() {
        return new Employee().new EmployeeBuilder();
    }

    public class EmployeeBuilder {
        private String hireDate;
        private String position;
        private String branchAddress;
        private User user;

        private EmployeeBuilder() {
        }

        public Employee.EmployeeBuilder setHireDate(String hireDate) {
            this.hireDate = new String(hireDate);
            return this;
        }

        public Employee.EmployeeBuilder setPosition(String position) {
            this.position = new String(position);
            return this;
        }

        public Employee.EmployeeBuilder setBranchAddress(String branchAddress) {
            this.branchAddress = new String(branchAddress);
            return this;
        }

        public Employee.EmployeeBuilder setUser(User user) {
            this.user = new User(user);
            return this;
        }

        public Employee build() {
            if ((hireDate != null) && (position != null) && (branchAddress != null) &&
                    (user != null)) {
                return new Employee(user, hireDate, position, branchAddress);
            }
            throw new IllegalStateException("Not all fields of UserData were initialized");
        }
    }

}
