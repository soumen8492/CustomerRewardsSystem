package CustomerRewardsApp.models;

public class TransactionAndPoints {
    private String dateOfTransaction;
    private int amountSpent;
    private int earnedPoints;

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(int amountSpent) {
        this.amountSpent = amountSpent;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public TransactionAndPoints(String dateOfTransaction, int amountSpent, int earnedPoints) {
        this.dateOfTransaction = dateOfTransaction;
        this.amountSpent = amountSpent;
        this.earnedPoints = earnedPoints;
    }
}
