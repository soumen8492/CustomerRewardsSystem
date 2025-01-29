package CustomerRewardsApp.models;

import java.util.ArrayList;
import java.util.List;

public class RewardDTO {
    private List<Reward> rewards_data;
    private int total_points;

    public List<Reward> getRewards_data() {
        return rewards_data;
    }

    public void setRewards_data(List<Reward> rewards_data) {
        this.rewards_data = rewards_data;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public RewardDTO() {
        this.rewards_data = new ArrayList<>();
    }

    public RewardDTO(List<Reward> rewards_data, int total_points) {
        this.rewards_data = rewards_data;
        this.total_points = total_points;
    }
}
