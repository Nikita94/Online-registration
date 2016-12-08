package supermed.httpexception;

import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by nikita on 08.12.2016.
 */
public class ResponseBuilderImpl{

    public static final String DATE_HEADER = "Date";

    public ResponseBuilderImpl() {
    }

    public Response ok() {
        return Response.ok().header(DATE_HEADER, new Date()).build();
    }

    public Response ok(Object responseObject) {
        return Response.ok(responseObject).header(DATE_HEADER, new Date()).build();
    }

    public Response status(Response.Status status) {
        return Response.status(status).build();
    }

    public Response accepted() {
        return Response.accepted().build();
    }

    public Response accepted(Object responseObject) {
        return Response.accepted(responseObject).build();
    }

    public Response badRequest(Object ent) {
        return Response.status(Response.Status.BAD_REQUEST).entity(ent).build();
    }

    public Response respondWithStatusAndObject(Response.Status errorStatus, Object ent) {
        return Response.status(errorStatus).entity(ent).build();
    }


    public Response serverError() {
        return Response.serverError().build();
    }

    public Response serverError(String message) {
        return Response.serverError().entity(message).build();
    }
}