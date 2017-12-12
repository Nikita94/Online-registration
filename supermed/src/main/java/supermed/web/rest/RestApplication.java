package supermed.web.rest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import supermed.consultancysystem.Visit;
import supermed.datamanagementsystem.DataManager;
import supermed.datamanagementsystem.impl.mongo.MongoDataManagerImpl;
import supermed.httpexception.ResourceNotFoundException;
import supermed.httpexception.ResponseBuilderImpl;
import supermed.statisticsframework.event.EventStatus;
import supermed.usermanagementsystem.UserManager;
import supermed.usermanagementsystem.impl.UserManagerImpl;
import supermed.usermanagementsystem.user.Employee;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;
import supermed.web.EmailSender;
import supermed.web.ui.PageWriter;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static supermed.usermanagementsystem.user.Employee.newBuilder;
import static supermed.usermanagementsystem.user.Role.*;

/**
 * Created by Alexander on 18.12.2016.
 */
@Path("/")
public class RestApplication extends Application {

    private UserManager userManagerImpl;
    private DataManager dataManager;
    private PageWriter pageWriter;
    private EmailSender emailSender = new EmailSender();

    @Context
    HttpServletRequest currentRequest;
    private ResponseBuilderImpl responseBuilder = new ResponseBuilderImpl();

    public RestApplication() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[]{"beans.xml"});
        userManagerImpl = (UserManager) context.getBean(UserManagerImpl.class);
        dataManager = (DataManager) context.getBean(MongoDataManagerImpl.class);
        pageWriter = (PageWriter) context.getBean(PageWriter.class);
    }

    @POST
    @Path("/users/visits")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String getSpecChooser(@FormParam("dateOfVisit") String visitDate, @FormParam
            ("branch")
            String branchID) {
        currentRequest.getSession().setAttribute("VisitDay", visitDate);
        currentRequest.getSession().setAttribute("VisitBranchID", branchID);
        return pageWriter.printSpecChooserForPatient(dataManager.getMedicalPositions(branchID));
    }

    @GET
    @Path("/users/visits/{specID}")
    @Produces(MediaType.TEXT_HTML)
    public String getScheduleForSpeciality(@PathParam("specID") String specID) {
        String visitDay = (String) currentRequest.getSession().getAttribute("VisitDay");
        String branchID = (String) currentRequest.getSession().getAttribute("VisitBranchID");
        return pageWriter.printShecduleForUser(dataManager.getSchedule(visitDay, branchID, specID));
    }

    @GET
    public Response defaultPage() {
        java.net.URI location = null;
        try {
            location = new java.net.URI("./login");
            return Response.temporaryRedirect(location).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @POST
    @Path("/create_patient")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response createPatient(@FormParam("first_name") String first_name,
                                  @FormParam("middle_name") String middle_name,
                                  @FormParam("last_name") String last_name,
                                  @FormParam("birth_date") String birth_date,
                                  @FormParam("address") String address,
                                  @FormParam("contact_phone") String contact_phone,
                                  @FormParam("email") String email,
                                  @FormParam("password") String password) {
        User currentUser = (User) currentRequest.getSession().getAttribute("User");
        if (currentUser.getRole() != MANAGER) {
            responseBuilder.respondWithStatusAndObject(Response.Status.CONFLICT, "You haven't " +
                    "enough " +
                    "permissions");
        } else {
            UserData userData = UserData.newBuilder().setFirstName(first_name)
                    .setMiddleName(middle_name)
                    .setLastName(last_name)
                    .setBirthDate(birth_date)
                    .setAddress(address)
                    .setPhoneNumber(contact_phone)
                    .setEmail(email)
                    .setLogin(email)
                    .build();
            User user = new User(userData, Role.PATIENT);
            String id = userManagerImpl.createUser(user, password);
            if (id.equals("")) {
                return responseBuilder.respondWithStatusAndObject(Response.Status.BAD_REQUEST,
                        "Incorrect data");
            } else {
                try {
                    /*emailSender.send("Добро пожаловать в клинику SuperMed!",
                            "Ваш аккаунт пациента успешно создан!\n\n" +
                                    "Спасибо за то, что к нам присоединились\n" +
                                    "Ваш логин и пароль для входа в систему: \n" +
                                    email + "\n" +
                                    password +
                                    "\n\n С уважением, команда Supermed", email);
                                    */
                    java.net.URI location = new java.net.URI("./users/" + id);
                    return Response.seeOther(location).build();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response logIn(@FormParam("login") String login,
                          @FormParam("password") String password) throws
            ResourceNotFoundException, NamingException {
        User user = userManagerImpl.logIn(login, password);
        java.net.URI location = null;
        if (user != null) {
            currentRequest.getSession().setAttribute("User", user);
            try {
                location = new java.net.URI("./users/" + user.getID());
                return Response.seeOther(location).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String logIn() {
        return pageWriter.printLoginPage();
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public String logOut() {
        currentRequest.getSession().removeAttribute("User");
        return pageWriter.printLoginPage();
    }


    @GET
    @Path("/create_patient")
    @Produces(MediaType.TEXT_HTML)
    public String createPatient() {
        return pageWriter.printCreatePatient();
    }

    @GET
    @Path("/show_profiles")
    @Produces(MediaType.TEXT_HTML)
    public String showProfiles() {
        User currentUser = (User) currentRequest.getSession().getAttribute("User");
        if (currentUser.getRole() != MANAGER) {
            responseBuilder.respondWithStatusAndObject(Response.Status.CONFLICT, "You haven't " +
                    "enough " +
                    "permissions");
        } else {
            List<User> userList = dataManager.getUsers();
            return pageWriter.printUsersProfile(userList);
        }
        return null;
    }

    @GET
    @Path("/create_employee")
    @Produces(MediaType.TEXT_HTML)
    public String createEmployee() {
        return pageWriter.printCreateEmployeePage();
    }


    @POST
    @Path("/create_employee")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response createEmployee(@FormParam("first_name") String first_name,
                                   @FormParam("middle_name") String middle_name,
                                   @FormParam("last_name") String last_name,
                                   @FormParam("birth_date") String birth_date,
                                   @FormParam("address") String address,
                                   @FormParam("contact_phone") String contact_phone,
                                   @FormParam("email") String email,
                                   @FormParam("password") String password,
                                   @FormParam("role") String role,
                                   @FormParam("position") String position,
                                   @FormParam("hireDate") String hireDate,
                                   @FormParam("branch") String branchAddress) {
        User currentUser = (User) currentRequest.getSession().getAttribute("User");
        if (currentUser.getRole() != MANAGER) {
            responseBuilder.respondWithStatusAndObject(Response.Status.CONFLICT, "You haven't " +
                    "enough " +
                    "permissions");
        } else {
            UserData userData = UserData.newBuilder().setFirstName(first_name)
                    .setMiddleName(middle_name)
                    .setLastName(last_name)
                    .setBirthDate(birth_date)
                    .setAddress(address)
                    .setPhoneNumber(contact_phone)
                    .setEmail(email)
                    .setLogin(email)
                    .build();
            User user = new User(userData,createRole(role));
            Employee employee = Employee.newBuilder()
                    .setUser(user)
                    .setHireDate(hireDate)
                    .setPosition(position)
                    .setBranchAddress(branchAddress)
                    .build();
            String id = userManagerImpl.createUser(employee, password);
            if (id.equals("")) {
                return responseBuilder.respondWithStatusAndObject(Response.Status.BAD_REQUEST,
                        "Incorrect data");
            } else {
                try {
                    /*emailSender.send("Добро пожаловать в клинику SuperMed!",
                            "Ваш аккаунт пациента успешно создан!\n\n" +
                                    "Спасибо за то, что к нам присоединились\n" +
                                    "Ваш логин и пароль для входа в систему: \n" +
                                    email + "\n" +
                                    password +
                                    "\n\n С уважением, команда Supermed", email);
                                    */
                    java.net.URI location = new java.net.URI("./users/" + id);
                    return Response.seeOther(location).build();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    @POST
    @Path("/update_yourself/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response updateUser(@PathParam("id") String id,
                               @FormParam("password") String password,
                               @FormParam("address") String address,
                               @FormParam("contact_phone") String contact_phone) {
        User user = userManagerImpl.getUserById(id);
        User currentUser = (User) currentRequest.getSession().getAttribute("User");
        if (user.getID().equals(currentUser.getID())) {
            try {
                userManagerImpl.updateInfoAboutYourself(id, password, address, contact_phone);
                /*emailSender.send("Изменение контактных данных",
                        "Ваши контактные данные были успешно изменены." +
                                "\n\n С уважением, команда Supermed", user.getUserData().getEmail());*/
                java.net.URI location = null;
                location = new java.net.URI("./users/" + id);
                return Response.seeOther(location).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String getUser(@PathParam("id") String id) {
        try {
            User currentUser = (User) currentRequest.getSession().getAttribute("User");
            if (currentUser != null) {
                if (currentUser.getRole() != PATIENT) {
                    return pageWriter.printUserProfilePage(userManagerImpl.getUserById(id));
                } else if (currentUser.getID().equals(id)) {
                    return pageWriter.printUserProfilePage(userManagerImpl.getUserById(id));
                }
            }
        } catch (Exception e) {
            return pageWriter.printErrorPage();
        }
        return pageWriter.printErrorPage();
    }

    @GET
    @Path("/remove/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String removeUser(@PathParam("id") String id) {
        try {
            User currentUser = (User) currentRequest.getSession().getAttribute("User");
            if (currentUser.getRole() == MANAGER) {
                User user = dataManager.getUserById(id);
                dataManager.removeUser(id);
              /*  emailSender.send("Спасибо за сотрудничество!\n",
                        "Спасибо за то, что были нашим клиентов все это время!" +
                                "\n\n С уважением, команда Supermed", user.getUserData().getEmail
                                ());*/
                List<User> userList = dataManager.getUsers();
                return pageWriter.printUsersProfile(userList);
            }
        } catch (Exception e) {

        }
        return pageWriter.printErrorPage();
    }

    @GET
    @Path("/update_yourself/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String getEditForm(@PathParam("id") String id) {
        User user = userManagerImpl.getUserById(id);
        try {
            User currentUser = (User) currentRequest.getSession().getAttribute("User");
            if (currentUser != null) {
                if (currentUser.getRole() != PATIENT && currentUser.getRole() != DOCTOR) {
                    return pageWriter.printEditForm(user);
                } else if (currentUser.getID().equals(id)) {
                    return pageWriter.printEditForm(user);
                }
            }
        } catch (Exception e) {

        }
        return pageWriter.printErrorPage();
    }

    @GET
    @Path("users/enlist")
    @Produces(MediaType.TEXT_HTML)
    public Response enlistForVisit(@QueryParam("doctorID") String doctorID, @QueryParam
            ("startTime") String
            sartTime, @QueryParam("endTime") String endTime) {
        try {
            User patient = (User) currentRequest.getSession().getAttribute("User");
            String branchID = (String) currentRequest.getSession().getAttribute("VisitBranchID");
            String visitDay = (String) currentRequest.getSession().getAttribute("VisitDay");
            User doctor = dataManager.getUserById(doctorID);
            if (patient != null) {
                dataManager.enlistForVisit(doctorID, patient.getID(), branchID, visitDay + " " +
                        sartTime, visitDay + " " + endTime);
                /*emailSender.send("Запись на прием", "Добавлена новая заявка на прием от пациента "
                        + patient.getUserData().getLastName() + " " + patient.getUserData()
                        .getFirstName() + " " + patient.getUserData().getMiddleName() + "\n " +
                        "Телефон: " + patient.getUserData().getPhoneNumber() + "\n" + "Электронная почта: "
                        + patient.getUserData().getEmail() + "\n", doctor.getUserData().getEmail());
                */

                java.net.URI location = new java.net.URI("./users/" + patient.getID());
                return Response.seeOther(location).build();

            }
        } catch (Exception e) {

        }
        return null;
    }

    @GET
    @Path("users/myVisits")
    @Produces(MediaType.TEXT_HTML)
    public String getVisits() {
        try {
            User user = (User) currentRequest.getSession().getAttribute("User");
            if (user != null) {
                List<Visit> plannedVisits = dataManager.getVisitsWithStatus(user, EventStatus
                        .PLANNED);
                List<Visit> finishedVisits = dataManager.getVisitsWithStatus(user, EventStatus
                        .FINISHED);
                Map<Visit, User> visits = new HashMap<Visit, User>();
                if (user.getRole().equals(Role.PATIENT)) {
                    User doctor;
                    for (Visit planned : plannedVisits) {
                        doctor = dataManager.getUserById(planned.getEmployeeID());
                        visits.put(planned, doctor);
                    }
                    for (Visit finished : finishedVisits) {
                        doctor = dataManager.getUserById(finished.getEmployeeID());
                        visits.put(finished, doctor);
                    }
                }
                if (user.getRole().equals(Role.DOCTOR)) {
                    User patient;
                    for (Visit planned : plannedVisits) {
                        patient = dataManager.getUserById(planned.getPatientID());
                        visits.put(planned, patient);
                    }
                    for (Visit finished : finishedVisits) {
                        patient = dataManager.getUserById(finished.getEmployeeID());
                        visits.put(finished, patient);
                    }
                }
                return pageWriter.printVisits(visits, user.getRole());
            }
        } catch (Exception e) {

        }
        return pageWriter.printLoginPage();
    }
}
