package usermanagementsystem.user;


/**
 * Created by Alexander on 22.11.2016.
 */
public class UserData {
    private String firstName;
    private String middleName;
    private String lastName;
    private String passportData;
    private String login;

    private UserData(String login, String firstName, String middleName, String lastName, String
            passportData) {
        this.login = login;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.passportData = passportData;
    }

    private UserData() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassportData() {
        return passportData;
    }

    public static UserDataBuilder newBuilder() {
        return new UserData().new UserDataBuilder();
    }

    public class UserDataBuilder {
        private String login;
        private String firstName;
        private String middleName;
        private String lastName;
        private String passportData;

        private UserDataBuilder() {
        }

        public UserDataBuilder setLogin(String login) {
            this.login = new String(login);
            return this;
        }

        public UserDataBuilder setFirstName(String firstName) {
            this.firstName = new String(firstName);
            return this;
        }

        public UserDataBuilder setMiddleName(String middleName) {
            this.middleName = new String(middleName);
            return this;
        }

        public UserDataBuilder setLastName(String lastName) {
            this.lastName = new String(lastName);
            return this;
        }

        public UserDataBuilder setPassportData(String passportData) {
            this.passportData = new String(passportData);
            return this;
        }

        public UserData build() {
            if ((firstName != null) && (middleName != null) && (lastName != null) &&
                    (passportData != null)) {
                return new UserData(login, firstName, middleName, lastName, passportData);
            }
            throw new IllegalStateException("Not all fields of UserData were initialized");
        }
    }

}
