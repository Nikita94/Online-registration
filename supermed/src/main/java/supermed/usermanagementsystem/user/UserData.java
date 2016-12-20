package supermed.usermanagementsystem.user;


import java.io.Serializable;

public class UserData implements Serializable {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;

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

    private UserData() {
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
