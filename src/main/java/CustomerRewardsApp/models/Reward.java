package CustomerRewardsApp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.HashMap;

@Entity
@Table(name = "REWARDPOINTS")
public class Reward {
    @Id
    @Column(name = "reward_id")
    private String rewardId;
    @Column(name = "cust_id")
    private String custId;
    @Column
    private String name;
    @Column(name = "mnth")
    private String month;
    @Column
    private int points;

    public Reward() {
    }

    public Reward(String rewardId, String custId, String name, String month, int points) {
        this.rewardId = rewardId;
        this.custId = custId;
        this.name = name;
        this.month = month;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Reward(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "RewardId='"+rewardId+'\''+
                "custId='" + custId + '\'' +
                ", name='" + name + '\'' +
                ", month='" + month + '\'' +
                ", points=" + points +
                '}';
    }
}
