package supermed.httpexception;

import javax.management.OperationsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import static javax.ws.rs.core.Response.Status.*;

/**
 * Created by nikita on 08.12.2016.
 */

public class OperationsExceptionMapper //implements ExceptionMapper<OperationsException> {
{

    /* private ResponseBuilderImpl responseBuilder = new ResponseBuilderImpl();

    @Override
    public Response toResponse(OperationsException ex) {
        if (ex instanceof ResourceNotFoundException)
            return responseBuilder.respondWithStatusAndObject(NOT_FOUND, ((ResourceNotFoundException) ex).getData());
        return Response.ok().build();
    }
    */
}
