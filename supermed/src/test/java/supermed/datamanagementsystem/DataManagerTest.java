package supermed.datamanagementsystem;


import static org.junit.Assert.*;

import org.junit.*;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import java.sql.*;
import java.util.Properties;


public class DataManagerTest {
    private DataManager dataManager = new DataManager();
    private Connection connection;
    private Statement statement;
    private User user = new User();
    private User resultUser = new User();
    private UserData.UserDataBuilder userData;
    @Before
    public void establishConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "root");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermed", connectionProps);
        statement = connection.createStatement();
    }
    @Before
    public void initialize(){
        user = new User();
        user.setID("0");
        UserData userData = UserData.newBuilder()
                .setFirstName("petr")
                .setMiddleName("petrovich")
                .setLastName("ivanov")
                .setLogin("petya@yandex.ru")
                .setBirthDate("01.01.2001")
                .setAddress("Pushkina street, Kookushkina house")
                .setPhoneNumber("88005553535")
                .build();
        user.setRole(Role.PATIENT);
        user.setUserData(userData);
    }


    @Test
    public void canSuccessfulConnection (){
        try {
            assertEquals (true, !connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canGetUserByLoginFromDB() throws SQLException {
        String login = "petya@yandex.ru";
        resultUser = dataManager.getUserByLogin(login, statement);
        assertEquals(user,resultUser);
    }

    @Test
    public void canGetUserByIdFromDB() throws SQLException {
        String id = "0";
        resultUser = dataManager.getUserById(id, statement);
        assertEquals(user,resultUser);
    }

    @Test
    public void canLogIn() throws SQLException {
        String login = "petya@yandex.ru";
        String password = "petya";
        resultUser = dataManager.logIn(login, password, statement);
        assertEquals(user,resultUser);
    }






}