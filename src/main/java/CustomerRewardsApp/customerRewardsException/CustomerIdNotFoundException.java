package CustomerRewardsApp.customerRewardsException;

public class CustomerIdNotFoundException extends RuntimeException{
    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private String errMsg;

    public CustomerIdNotFoundException(String message, String errMsg) {
        super(message);
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "{\n" +
                "errMsg='" + errMsg + '\'' +
                "\nmessage='" + super.getMessage() + '\'' +
                "\n}";
    }
}
