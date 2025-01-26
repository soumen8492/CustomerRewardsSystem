package CustomerRewardsApp.customerRewardsException;

public class TransactionNotFoundException extends RuntimeException{
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "{" +
                "errMsg='" + errMsg + '\'' +
                "\nMessage='" + super.getMessage() + '\'' +
                '}';
    }

    public TransactionNotFoundException(String message, String errMsg) {
        super(message);
        this.errMsg = errMsg;
    }
}
