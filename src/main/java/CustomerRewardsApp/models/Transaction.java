package CustomerRewardsApp.models;

public class Transaction {
    private String TransactionId;
    private String custId;
    private int amount;
    private String monthOfTransaction;

    public Transaction() {
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMonthOfTransaction() {
        return monthOfTransaction;
    }

    public void setMonthOfTransaction(String monthOfTransaction) {
        this.monthOfTransaction = monthOfTransaction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "TransactionId='" + TransactionId + '\'' +
                ", custId='" + custId + '\'' +
                ", amount=" + amount +
                ", monthOfTransaction='" + monthOfTransaction + '\'' +
                '}';
    }
}
