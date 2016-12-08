package supermed.usermanagementsystem;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Alexander on 24.11.2016.
 */
@Path("/users")
public class UserManager extends Application {
    public UserManager() {

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logIn(@FormParam("login") String login,
                          @FormParam("password") String password) {
        return Response.status(200).entity("User have " + login + " and "+ password).build();
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

    //@GET
    //@Path("/{id}")
    //public User getUser(String login) {
        //return null;
    //}

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
