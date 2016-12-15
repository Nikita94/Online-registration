package supermed.usermanagementsystem.impl;

import supermed.datamanagementsystem.DataManager;
import supermed.httpexception.ResourceNotFoundException;
import supermed.httpexception.ResponseBuilderImpl;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URISyntaxException;

import static supermed.usermanagementsystem.user.Role.*;

/**
 * Created by Alexander on 24.11.2016.
 */
@Path("/")
public class UserManager extends Application {
    @Context
    HttpServletRequest currentRequest;
    private ResponseBuilderImpl responseBuilder = new ResponseBuilderImpl();

    public UserManager() {

    }

    @GET
    public Response defaultePage() {
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
            responseBuilder.respondWithStatusAndObject(Status.CONFLICT, "You haven't enough permissions");
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
            boolean wasExecuted = DataManager.createUser(user, password);
            if (!wasExecuted) {
                return responseBuilder.respondWithStatusAndObject(Status.BAD_REQUEST, "Incorrect data");
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
        User user = DataManager.logIn(login, password);
        java.net.URI location = null;
        if (user != null) {
            currentRequest.getSession().setAttribute("User", user);
            currentRequest.getSession().setAttribute("isAuthorized", Boolean.TRUE);
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
        currentRequest.getSession().removeAttribute("isAuthorized");
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
        User user = DataManager.getUserById(id);
        //User currentUser = (User) currentRequest.getAttribute("User");
        //if (user.getID().equals(currentUser.getID())) {
            DataManager.updateInfoAboutYourself(id, password, address, contact_phone);
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
            if (currentRequest.getSession().getAttribute("isAuthorized") != null) {
                User currentUser = (User) currentRequest.getSession().getAttribute("User");
                if (currentUser.getRole() != PATIENT) {
                    return PageWriter.printUserProfilePage(DataManager.getUserById(id));
                } else if (currentUser.getID().equals(id)) {
                    return PageWriter.printUserProfilePage(DataManager.getUserById(id));
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
        User user = DataManager.getUserById(id);
        try {
            if (currentRequest.getSession().getAttribute("isAuthorized") != null) {
                return PageWriter.printEditForm(user);
            }
        } catch (Exception e) {

        }
        return PageWriter.printErrorPage();
    }
}
