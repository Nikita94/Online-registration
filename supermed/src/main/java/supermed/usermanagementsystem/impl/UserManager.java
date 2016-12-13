package supermed.usermanagementsystem.impl;

import supermed.datamanagementsystem.DataManager;
import supermed.httpexception.ResourceNotFoundException;
import supermed.httpexception.ResponseBuilderImpl;
import supermed.usermanagementsystem.UserService;
import supermed.usermanagementsystem.user.User;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URISyntaxException;

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

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logIn(@FormParam("login") String login,
                          @FormParam("password") String password) throws
            ResourceNotFoundException, NamingException {
        User user = DataManager.logIn(login, password);
        if (user != null) {
            currentRequest.getSession().setAttribute("isAuthorized", Boolean.TRUE);
            java.net.URI location = null;
            try {
                location = new java.net.URI("./users/" + user.getID());
                return Response.temporaryRedirect(location).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return responseBuilder.respondWithStatusAndObject(Status.NOT_FOUND, "User not found");
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String logIn() {
        return PageWriter.printLoginPage();
    }
//
    //@POST
    //@Consumes(MediaType.APPLICATION_JSON)
    //public boolean signIn(User user) {
    //    return false;
    //}
//
    //@POST
    //@Consumes(MediaType.APPLICATION_JSON)
    //public boolean updateUser(User user) {
    //    return false;
    //}
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
                return PageWriter.printUserProfilePage(DataManager.getUserById(id));
            }
        } catch (Exception e) {

        }
        return PageWriter.printErrorPage();
    }

    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // public boolean crateUser(User user, String password) {
    //     return false;
    // }
}
