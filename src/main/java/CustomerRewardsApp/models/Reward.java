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

    @Column(name = "transaction_id")
    private String transactionId;
    @Column
    private int points;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public Reward() {
    }

    public Reward(String rewardId, String transactionId, int points) {
        this.rewardId = rewardId;
        this.transactionId = transactionId;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "RewardId='"+rewardId+'\''+
                "custId='" + transactionId + '\'' +
                ", points=" + points +
                '}';
    }
}
