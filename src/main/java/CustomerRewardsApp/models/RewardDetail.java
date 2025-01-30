package CustomerRewardsApp.models;

public class RewardDetail {
    private String month;
    private int points;
    private int amountSpent;

    public int getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(int amountSpent) {
        this.amountSpent = amountSpent;
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

    public RewardDetail(String month, int points) {
        this.month = month;
        this.points = points;
    }

    public RewardDetail(String month, int points, int amountSpent) {
        this.month = month;
        this.points = points;
        this.amountSpent = amountSpent;
    }
}
