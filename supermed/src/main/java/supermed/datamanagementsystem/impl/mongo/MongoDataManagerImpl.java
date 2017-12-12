package supermed.datamanagementsystem.impl.mongo;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import supermed.consultancysystem.Visit;
import supermed.datamanagementsystem.DataManager;
import supermed.statisticsframework.Schedule;
import supermed.statisticsframework.event.EventStatus;
import supermed.usermanagementsystem.user.Employee;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MongoDataManagerImpl implements DataManager {

    private static final String TABLE_USERS = "users";

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private MongoSequence idGenerator;
    @Override
    public User getUserByLogin(String login) {
        return mongoOperations.findOne(Query.query(Criteria.where("login").is(login)), User.class);
    }

    @Override
    public User logIn(String login, String password) {
        User user = mongoOperations.findOne(Query.query(Criteria.where("login").is(login).
                and("password").is(password)), User.class);
        if(user==null){return null;}
        if (!user.getRole().equals(Role.PATIENT)) {
            user = mongoOperations.findOne(Query.query(Criteria.where("login").is(login).
                    and("password").is(password)), Employee.class);
        }
        return user;
    }

    @Override
    public User getUserById(String id) {
        User user = mongoOperations.findOne(Query.query(Criteria.where("userID").is(id)), User.class);
        if (!user.getRole().equals(Role.PATIENT)) {
            user = mongoOperations.findOne(Query.query(Criteria.where("userID").is(id)), Employee.class);
        }
        return user;
    }

    @Override
    public Map<String, String> getMedicalPositions(String branchId) {
        final String[] branchAddress = new String[1];
        final Map<String, String> positions = new HashMap<String, String>();
        final Map<String, String> output = new HashMap<String, String>();
        MongoCursor<Document> cursor = mongoOperations.getCollection("branches").find().iterator();
        cursor.forEachRemaining(document -> {
            if (document.getString("branchId").equals(branchId)) {
                branchAddress[0] = document.getString("branchAddress");
            }
        });
        cursor = mongoOperations.getCollection("positions").find().iterator();
        cursor.forEachRemaining(document -> positions.put(document.getString("positionName"),
                document.getString("positionID")));
        Query query = new Query(Criteria.where("role").is("DOCTOR"));
        List<Employee> doctors = mongoOperations.find(query, Employee.class);
        for (Employee doctor : doctors) {
            if (doctor.getBranchAddress().equals(branchAddress[0])) {
                output.put(positions.get(doctor.getPosition()), doctor.getPosition());
            }
        }
        return output;
    }


    @Override
    public String createUser(User user, String password) {
        String id = idGenerator.getNextID("users");
        user.setID(id);
        mongoOperations.insert(user,"users");
        Update mergeUserUpdate = new Update();
        mergeUserUpdate.set("password", password);
        mergeUserUpdate.set("login", user.getUserData().getEmail());
        mongoOperations.upsert(new Query(Criteria.where("userID").is(id)), mergeUserUpdate, "users").wasAcknowledged();
        return id;
    }

    @Override
    public boolean updateInfoAboutYourself(String id, String password, String address, String contact_phone) {
        Update mergeUserUpdate = new Update();
        mergeUserUpdate.set("password", password);
        mergeUserUpdate.set("userData.address", address);
        mergeUserUpdate.set("userData.contact_phone", contact_phone);
        return mongoOperations.upsert(new Query(Criteria.where("userID").is(id)), mergeUserUpdate, "users").wasAcknowledged();
    }

    @Override
    public Map<String, String> getBranches() {
        Map<String, String> branches = new HashMap<String, String>();
        MongoCursor<Document> cursor = mongoOperations.getCollection("branches").find().iterator();
        cursor.forEachRemaining(document -> branches.put(document.getString("branchID"),
                document.getString("address")));
        return branches;
    }

    @Override
    public List<Schedule> getSchedule(String day, String branchId, String positionID) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> output = new ArrayList<User>();
        List<User> users = mongoOperations.findAll(User.class);
        for (User user : users) {
            if (!user.getRole().equals(Role.PATIENT)) {
                output.add(getUserById(user.getID()));
            } else {
                output.add(user);
            }
        }
        return output;
    }

    @Override
    public List<String> getAllId() {
        List<String> output = new ArrayList<String>();
        List<User> users = mongoOperations.findAll(User.class);
        for (User user : users) {
            output.add(user.getID());
        }
        return output;
    }

    @Override
    public void removeUser(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoOperations.remove(query, User.class);
    }

    @Override
    public void enlistForVisit(String doctorID, String patientID, String branchID, String startTime, String endTime) {

    }

    @Override
    public List<Visit> getVisitsWithStatus(User user, EventStatus status) {
        return null;
    }


    private List<Employee> getAllEmployees() {
        Query query = new Query(Criteria.where("role").not().is("PATIENT"));
        return mongoOperations.find(query, Employee.class);
    }
}
