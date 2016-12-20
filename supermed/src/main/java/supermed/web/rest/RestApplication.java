package supermed.web.rest;

import supermed.datamanagementsystem.DataManager;
import supermed.httpexception.ResourceNotFoundException;
import supermed.httpexception.ResponseBuilderImpl;
import supermed.usermanagementsystem.UserManager;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;
import supermed.web.ui.PageWriter;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.List;

import static supermed.usermanagementsystem.user.Role.*;

/**
 * Created by Alexander on 18.12.2016.
 */
@Path("/")
public class RestApplication extends Application {

    UserManager userManager = new UserManager();
    DataManager dataManager = new DataManager();
    PageWriter pageWriter = new PageWriter();

    @Context
    HttpServletRequest currentRequest;
    private ResponseBuilderImpl responseBuilder = new ResponseBuilderImpl();

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
    public Response createUser(@FormParam("first_name") String first_name,
                               @FormParam("middle_name") String middle_name,
                               @FormParam("last_name") String last_name,
                               @FormParam("birth_date") String birth_date,
                               @FormParam("address") String address,
                               @FormParam("contact_phone") String contact_phone,
                               @FormParam("role") String role,
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
            User user = new User(userData, createRole(role));
            String id = userManager.createUser(user, password);
            if (id.equals("")) {
                return responseBuilder.respondWithStatusAndObject(Response.Status.BAD_REQUEST,
                        "Incorrect data");
            } else {
                try {
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
        User user = userManager.logIn(login, password);
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
    public String createUser() {
        return pageWriter.printCreateUserPage();
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
    public String createDoctor() {
        return pageWriter.printCreateEmployeePage();
    }

    @POST
    @Path("/update_yourself/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response updateUser(@PathParam("id") String id,
                               @FormParam("password") String password,
                               @FormParam("address") String address,
                               @FormParam("contact_phone") String contact_phone) {
        User user = userManager.getUserById(id);
        //User currentUser = (User) currentRequest.getAttribute("User");
        //if (user.getID().equals(currentUser.getID())) {
        userManager.updateInfoAboutYourself(id, password, address, contact_phone);
        java.net.URI location = null;
        try {
            location = new java.net.URI("./users/" + id);
            return Response.seeOther(location).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //}
        return null;
    }
//
    //@DELETE
    //@Path("/{id}")
    //public boolean deleteUser(User user) {
    //    return false;
    //}

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String getUser(@PathParam("id") String id) {
        try {
            User currentUser = (User) currentRequest.getSession().getAttribute("User");
            if (currentUser != null) {
                if (currentUser.getRole() != PATIENT) {
                    return pageWriter.printUserProfilePage(userManager.getUserById(id));
                } else if (currentUser.getID().equals(id)) {
                    return pageWriter.printUserProfilePage(userManager.getUserById(id));
                }
            }
        } catch (Exception e) {

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
                dataManager.removeUser(id);
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
        User user = userManager.getUserById(id);
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
            if (patient != null) {
                dataManager.enlistForVisit(doctorID, patient.getID(), branchID, visitDay + " " +
                        sartTime, visitDay + " " + endTime);
                java.net.URI location = new java.net.URI("./users/" + patient.getID());
                return Response.seeOther(location).build();

            }
        } catch (Exception e) {

        }
        return null;
    }
}
