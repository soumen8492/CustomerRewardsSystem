package CustomerRewardsApp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUSTOMERTRANSACTION")
public class Transaction {
    @Id
    @Column(name = "tran_id")
    private String TransactionId;
    @Column(name="cust_id")
    private String custId;
    @Column(name="amount")
    private int amount;
    @Column(name="mnth")
    private String monthOfTransaction;
    @Column(name="points")
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Transaction() {
    }

    public Transaction(String transactionId, String custId, int amount, String monthOfTransaction, int points) {
        TransactionId = transactionId;
        this.custId = custId;
        this.amount = amount;
        this.monthOfTransaction = monthOfTransaction;
        this.points = points;
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
                ", points=" + points +
                ", monthOfTransaction='" + monthOfTransaction + '\'' +
                '}';
    }
}
