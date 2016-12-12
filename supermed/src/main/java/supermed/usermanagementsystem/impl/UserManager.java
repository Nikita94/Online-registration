package supermed.usermanagementsystem.impl;

import supermed.httpexception.ResourceNotFoundException;
import supermed.httpexception.ResponseBuilderImpl;
import supermed.datamanagementsystem.DataManager;
import supermed.usermanagementsystem.UserService;
import supermed.usermanagementsystem.user.User;

import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;

/**
 * Created by Alexander on 24.11.2016.
 */
@Path("/users")
public class UserManager extends Application {

    protected UserService userService = new UserServiceImpl();
    private ResponseBuilderImpl responseBuilder = new ResponseBuilderImpl();

    public UserManager() {

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logIn(@FormParam("login") String login,
                          @FormParam("password") String password) throws
            ResourceNotFoundException, NamingException {
        User user = userService.logIn(login, password);
        if (user != null)
            return responseBuilder.respondWithStatusAndObject(Status.OK, user);
        else
            return responseBuilder.respondWithStatusAndObject(Status.NOT_FOUND, "User not found");
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
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String getUser(@QueryParam("id") String id) {
        /*User user = new User();
        Role role = Role.createRole("patient");
        UserData userData = UserData.newBuilder()
                .setFirstName("Иван")
                .setMiddleName("Иванович")
                .setLastName("Иванов")
                .setLogin("vanya@yandex.ru")
                .setBirthDate("01.01.2001")
                .setAddress("none")
                .setPhoneNumber("none")
                .build();

        user.setRole(role);
        user.setUserData(userData);*/
        try {
            ArrayList<User> users = (ArrayList<User>) DataManager.getUsers();
            return PageWriter.printUserProfilePage(users.get(0));
        } catch (Exception e) {

        }
        return null;
    }

    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // public boolean crateUser(User user, String password) {
    //     return false;
    // }

    // This method is called if TEXT_PLAIN is request
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello Jersey";
    }

    // This method is called if XML is request
    @GET
    @Produces(MediaType.TEXT_XML)
    public String sayXMLHello() {
        return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
    }

    // This method is called if HTML is request
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
        return "<html> " + "<title>" + "Hello Jersey" + "</title>"
                + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
    }
}
