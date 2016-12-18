package supermed.web.rest;

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

import static supermed.usermanagementsystem.user.Role.MANAGER;
import static supermed.usermanagementsystem.user.Role.PATIENT;
import static supermed.usermanagementsystem.user.Role.createRole;

/**
 * Created by Alexander on 18.12.2016.
 */
@Path("/")
public class RestApplication extends Application {
    @Context
    HttpServletRequest currentRequest;
    private ResponseBuilderImpl responseBuilder = new ResponseBuilderImpl();

    @POST
    @Path("/users/visits")
    @Produces(MediaType.TEXT_HTML)
    public String getSchedule(@FormParam("dateOfVisit") String visitDate, @FormParam
            ("branch")
            String branchID) {
        currentRequest.getSession().setAttribute("VisitDay", visitDate);
        currentRequest.getSession().setAttribute("VisitBranchID", branchID);
        return "Stub String for schedule on  " + visitDate + " and branchID=" + branchID;
    }

    @GET
    @Path("/users/visits")
    @Produces(MediaType.TEXT_HTML)
    public String getScheduleForSpeciality() {
        return "";
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
    @Path("/create_user")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createUser(@FormParam("first_name") String first_name,
                               @FormParam("middle_name") String middle_name,
                               @FormParam("last_name") String last_name,
                               @FormParam("birth_date") String birth_date,
                               @FormParam("address") String address,
                               @FormParam("contact_phone") String contact_phone,
                               @FormParam("role") String role,
                               @FormParam("email") String email,
                               @FormParam("password") String password) {
        User currentUser = (User) currentRequest.getAttribute("User");
        if (currentUser.getRole() == MANAGER) {
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
            boolean wasExecuted = UserManager.createUser(user, password);
            if (!wasExecuted) {
                return responseBuilder.respondWithStatusAndObject(Response.Status.BAD_REQUEST,
                        "Incorrect " +
                                "data");
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
        User user = UserManager.logIn(login, password);
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
        return PageWriter.printLoginPage();
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public String logOut() {
        currentRequest.getSession().removeAttribute("User");
        return PageWriter.printLoginPage();
    }


    @GET
    @Path("/create_user")
    @Produces(MediaType.TEXT_HTML)
    public String createUser() {
        return PageWriter.printCreateUserPage();
    }

    @POST
    @Path("/update_yourself/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response updateUser(@PathParam("id") String id,
                               @FormParam("password") String password,
                               @FormParam("address") String address,
                               @FormParam("contact_phone") String contact_phone) {
        User user = UserManager.getUserById(id);
        //User currentUser = (User) currentRequest.getAttribute("User");
        //if (user.getID().equals(currentUser.getID())) {
        UserManager.updateInfoAboutYourself(id, password, address, contact_phone);
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
            if (currentRequest.getSession().getAttribute("User") != null) {
                User currentUser = (User) currentRequest.getSession().getAttribute("User");
                if (currentUser.getRole() != PATIENT) {
                    return PageWriter.printUserProfilePage(UserManager.getUserById(id));
                } else if (currentUser.getID().equals(id)) {
                    return PageWriter.printUserProfilePage(UserManager.getUserById(id));
                }
            }
        } catch (Exception e) {

        }
        return PageWriter.printErrorPage();
    }

    @GET
    @Path("/update_yourself/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String getEditForm(@PathParam("id") String id) {
        User user = UserManager.getUserById(id);
        try {
            if (currentRequest.getSession().getAttribute("User") != null) {
                return PageWriter.printEditForm(user);
            }
        } catch (Exception e) {

        }
        return PageWriter.printErrorPage();
    }
}