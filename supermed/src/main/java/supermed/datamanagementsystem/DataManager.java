package supermed.datamanagementsystem;

import org.springframework.stereotype.Component;
import supermed.consultancysystem.Visit;
import supermed.statisticsframework.Schedule;
import supermed.statisticsframework.event.EventStatus;
import supermed.usermanagementsystem.user.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Alexander on 22.12.2016.
 */

public interface DataManager {

    User getUserByLogin(String login);

    User logIn(String login, String password);

    User getUserById(String id);

    Map<String, String> getMedicalPositions(String branchId);

    String createUser(User user, String password);

    boolean updateInfoAboutYourself(String id, String password, String address,
                                    String contact_phone);

    Map<String, String> getBranches();

    List<Schedule> getSchedule(String day, String branchId, String positionID);

    List<User> getUsers();

    List<String> getAllId();

    void removeUser(String id);

    void enlistForVisit(String doctorID, String patientID, String branchID, String
            startTime, String endTime);

    List<Visit> getVisitsWithStatus(User user, EventStatus status);
}
