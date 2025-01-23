package CustomerRewardsApp.models;

public class Reward {
    private String custId;
    private String month;
    private int point;

    public Reward(String custId, int point) {
        this.custId = custId;
        this.point = point;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Reward() {
    }

    public Reward(String custId, String month, int point) {
        this.custId = custId;
        this.month = month;
        this.point = point;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "custId='" + custId + '\'' +
                ", month='" + month + '\'' +
                ", point=" + point +
                '}';
    }
}
