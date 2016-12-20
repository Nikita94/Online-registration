package supermed.datamanagementsystem;


import supermed.statisticsframework.Event;
import supermed.statisticsframework.Schedule;
import supermed.usermanagementsystem.user.Employee;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nikita on 08.12.2016.
 */
public class DataManager {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private InitialContext initialContext;
    private DataSource dataSource;

    public DataManager() {
    }

    private void openConnection() {
        // opening database connection to MySQL server
        try {
            initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/supermed");
            connection = dataSource.getConnection();
            // getting Statement object to execute query
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws SQLException, NamingException {
        if (resultSet != null)
            resultSet.close();
        if (statement != null)
            statement.close();
        if (connection != null)
            connection.close();
        if (initialContext != null)
            initialContext.close();
    }


    public User getUserByLogin(String login) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from users where login = '" + login + "'");
            return constructUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User logIn(String login, String password) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from users where login = '" + login +
                    "' and password = '" + password + "'");
            return constructUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User getUserById(String id) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from users where id = " + id);
            return constructUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, String> getMedicalPositions(String branchId) {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from positions where is_medical = 1 " +
                    "and id in (select position_id from employees where branch_id =" + branchId +
                    " ) ");
            Map<String, String> medicalPostitions = new HashMap<String, String>();
            while (resultSet.next()) {
                medicalPostitions.put(resultSet.getString("id"), resultSet.getString("name"));
            }
            return medicalPostitions;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private User constructUser() {
        User user = new User();
        try {
            if (resultSet.next()) {
                Role role = Role.createRole(resultSet.getString("role"));
                UserData userData = UserData.newBuilder()
                        .setFirstName(resultSet.getString("first_name"))
                        .setMiddleName(resultSet.getString("middle_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setLogin(resultSet.getString("login"))
                        .setBirthDate(resultSet.getString("birth_date"))
                        .setAddress(resultSet.getString("address"))
                        .setPhoneNumber(resultSet.getString("contact_phone"))
                        .build();
                user.setRole(role);
                user.setUserData(userData);
                user.setID(resultSet.getString("id"));
                if (!user.getRole().equals(Role.PATIENT)) {
                    return constructEmployee(user);
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee constructEmployee(User user) {

        try {
            resultSet = statement.executeQuery("select e.hire_date, b.address, p.name from " +
                    "employees e, branches b, positions p where e.id = " +
                    user.getID() + " and b.id = e.branch_id and p.id = e.position_id");
            if (resultSet.next()) {
                return Employee.newBuilder()
                        .setUser(user)
                        .setPosition(resultSet.getString("name"))
                        .setBranchAddress(resultSet.getString("address"))
                        .setHireDate(resultSet.getString("hire_date"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(User user, String password) {
        UserData userData = user.getUserData();
        String query = "INSERT INTO `users` VALUES (NULL,'" +
                userData.getLogin() + "','" +
                password + "','" +
                userData.getFirstName() + "','" +
                userData.getMiddleName() + "','" +
                userData.getLastName() + "','" +
                userData.getBirthDate() + "','" +
                userData.getAddress() + "','" +
                userData.getPhoneNumber() + "','" +
                user.getRole().getName() + "');";
        return executeUpdateQuery(query);
    }

    public boolean updateInfoAboutYourself(String id, String password, String address,
                                           String contact_phone) {
        String query = "UPDATE users SET password = \"" +
                password + "\", address = \"" +
                address + "\", contact_phone = \"" +
                contact_phone + "\" WHERE id = " + id;
        return executeUpdateQuery(query);
    }

    private boolean executeUpdateQuery(String query) {
        openConnection();
        try {
            if (statement.executeUpdate(query) != 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Map<String, String> getBranches() {
        openConnection();
        try {
            resultSet = statement.executeQuery("select * from branches");
            Map<String, String> output = new HashMap<String, String>();
            while (resultSet.next()) {
                output.put(resultSet.getString("id"), resultSet.getString("address"));
            }
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public List<Schedule> getSchedule(String day, String branchId, String positionID) {
        openConnection();
        try {
            List<Employee> doctors = new LinkedList<Employee>();
            resultSet = statement.executeQuery("select * from users where id in (select id from " +
                    "employees where branch_id = " +
                    branchId +
                    " and position_id = " + positionID + ");");
            Employee employee = (Employee) constructUser();
            while (employee != null) {
                doctors.add(employee);
                employee = (Employee) constructUser();
            }

            resultSet = statement.executeQuery("select * from events where branch_id = " +
                    branchId +
                    " and user_id in (select id from employees where branch_id = " + branchId +
                    " and position_id = " + positionID + ") and (actual_start_date like '" + day
                    + "%' or actual_end_date like '" + day + "%')");
            List<Event> events = new LinkedList<Event>();
            Event event = constructEvent();
            while (event != null) {
                events.add(event);
                event = constructEvent();
            }
            List<Schedule> schedules = new LinkedList<Schedule>();
            for (Employee empl : doctors) {
                Schedule schedule = new Schedule();
                schedule.setEmployee(empl);
                for (Event ev : events) {
                    if (empl.getID().equals(ev.getEmployeeID())) {
                        schedule.addEvent(ev);
                    }
                }
                schedules.add(schedule);
            }
            return schedules;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Event constructEvent() {
        try {
            if (resultSet.next()) {
                return new Event(resultSet.getString("id"), resultSet.getString("user_id"),
                        resultSet.getString("expected_start_date"),
                        resultSet.getString("expected_end_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
