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
    private String transactionId;
    @Column(name="cust_id")
    private String custId;
    @Column(name = "name")
    private String name;
    @Column(name="amount")
    private int amount;
    @Column(name="tran_date")
    private String tranDate;

    public Transaction() {
    }

    public Transaction(String transactionId, String custId, String name, String tranDate, int amount) {
        this.transactionId = transactionId;
        this.custId = custId;
        this.amount = amount;
        this.tranDate = tranDate;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        transactionId = transactionId;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "TransactionId='" + transactionId + '\'' +
                ", custId='" + custId + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", tranDate='" + tranDate + '\'' +
                '}';
    }
}
