package pizzashop.exception;

public class ServiceException extends RuntimeException {

    private final String message;

    public ServiceException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
