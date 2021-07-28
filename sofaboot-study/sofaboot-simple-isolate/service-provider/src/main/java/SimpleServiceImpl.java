/**
 * @author 淡漠
 */
public class SimpleServiceImpl implements SimpleService{
    private String message;

    public SimpleServiceImpl(String message) {
        this.message = message;
    }

    public SimpleServiceImpl(){}

    @Override
    public String message() {
        System.out.println(message);
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
