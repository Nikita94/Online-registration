package supermed.httpexception;

import javax.management.OperationsException;

/**
 * Created by nikita on 08.12.2016.
 */
public class ResourceNotFoundException extends OperationsException {

    private final Object data;

    public ResourceNotFoundException(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
