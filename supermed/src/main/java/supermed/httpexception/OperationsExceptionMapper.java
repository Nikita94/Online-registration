package supermed.httpexception;

import javax.management.OperationsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by nikita on 08.12.2016.
 */

public class OperationsExceptionMapper implements ExceptionMapper<OperationsException> {

    private Response.ResponseBuilder responseBuilder;

    @Override
    public Response toResponse(OperationsException ex) {
        if (ex instanceof IncompatibleActionException)
            return responseBuilder.respondWithStatusAndObject(NOT_FOUND, ((IncompatibleActionException) ex).getData());
        else if (ex instanceof ResourceNotFoundException)
            return responseBuilder.respondWithStatusAndObject(NOT_FOUND, ((ResourceNotFoundException) ex).getData());
        else if (ex instanceof RequestDataValidationException)
            return responseBuilder.badRequest(((RequestDataValidationException) ex).getData());
        else if (ex instanceof TaskSubmitException)
            return responseBuilder.respondWithStatusAndObject(CONFLICT, ((TaskSubmitException) ex).getConflict());
        else if (ex instanceof ForbiddenActionException)
            return responseBuilder.respondWithStatusAndObject(FORBIDDEN, ex.getMessage());
        else if (ex instanceof ConflictOperationsException)
            return responseBuilder.respondWithStatusAndObject(CONFLICT, ((ConflictOperationsException) ex).getData());
        else if (ex instanceof BrokenConfigurationException)
            return responseBuilder.serverError(Constants.CONFIGURATION_BROKEN);
        else return responseBuilder.serverError(ex.getMessage());
    }
}
