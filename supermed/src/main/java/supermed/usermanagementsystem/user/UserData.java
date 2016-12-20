package supermed.usermanagementsystem.user;


/**
 * Created by Alexander on 22.11.2016.
 */

public class UserData {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserData userData = (UserData) o;

        if (email != null ? !email.equals(userData.email) : userData.email != null) return false;
        if (firstName != null ? !firstName.equals(userData.firstName) : userData.firstName != null)
            return false;
        if (middleName != null ? !middleName.equals(userData.middleName) : userData.middleName !=
                null)
            return false;
        if (lastName != null ? !lastName.equals(userData.lastName) : userData.lastName != null)
            return false;
        if (birthDate != null ? !birthDate.equals(userData.birthDate) : userData.birthDate != null)
            return false;
        if (address != null ? !address.equals(userData.address) : userData.address != null)
            return false;
        return phoneNumber != null ? phoneNumber.equals(userData.phoneNumber) : userData
                .phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    private UserData(String email, String firstName, String middleName, String lastName, String
            birthDate, String address, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public UserData() {
    }

    public String getLogin() {
        return email;
    }

    public String getEmail() {
        return email;
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

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public static UserDataBuilder newBuilder() {
        return new UserData().new UserDataBuilder();
    }

    public class UserDataBuilder {
        private String email;
        private String firstName;
        private String middleName;
        private String lastName;
        private String birthDate;
        private String address;
        private String phoneNumber;

        private UserDataBuilder() {
        }

        public UserDataBuilder setEmail(String email) {
            this.email = new String(email);
            return this;
        }

        public UserDataBuilder setLogin(String email) {
            this.email = new String(email);
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

        public UserDataBuilder setBirthDate(String birthDate) {
            this.birthDate = new String(birthDate);
            return this;
        }

        public UserDataBuilder setAddress(String address) {
            this.address = new String(address);
            return this;
        }

        public UserDataBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = new String(phoneNumber);
            return this;
        }

        public UserData build() {
            if ((firstName != null) && (middleName != null) && (lastName != null) &&
                    (email != null) && (birthDate != null) && (address != null)
                    && (phoneNumber != null)) {
                return new UserData(email, firstName, middleName, lastName, birthDate, address,
                        phoneNumber);
            }
            throw new IllegalStateException("Not all fields of UserData were initialized");
        }
    }

}
