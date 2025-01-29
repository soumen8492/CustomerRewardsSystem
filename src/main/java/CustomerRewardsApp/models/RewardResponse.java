package CustomerRewardsApp.models;

import java.util.ArrayList;
import java.util.List;

public class RewardResponse {

    private String custId;
    private  String name;
    private List<RewardDetail> rewardDetails;
    private int total_points;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RewardDetail> getRewardDetails() {
        return rewardDetails;
    }

    public void setRewardDetails(List<RewardDetail> rewardDetails) {
        this.rewardDetails = rewardDetails;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

}
