package Model;

public class Pod {
    private int X;
    private int Y;
    private int belongsBlockId;
    private String podCode;
    private String agvCode;
    private String assignedAgvCode;

    /**
     * 储存货架是否被扛起的信息
     */
    private boolean isLoading = false;

    /**
     * 储存货架是否被分配的信息
     */
    private boolean isAssigned = false;

    public Pod(String podCode1,int x,int y){
        podCode = podCode1;
        X = x;
        Y = y;
    }
    public int getX() {
        return X;
    }
    public void setX(int x) {
        X = x;
    }
    public int getY() {
        return Y;
    }
    public void setY(int y) {
        Y = y;
    }
    public String getPodCode() {
        return podCode;
    }
    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    public String getAgvCode() {
        return agvCode;
    }
    public void setAgvCode(String agvCode) {
        this.agvCode = agvCode;
    }
    public boolean isAssigned() {
        return isAssigned;
    }
    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
    public int getBelongsBlockId() {
        return belongsBlockId;
    }
    public void setBelongsBlockId(int belongsBlockId) {
        this.belongsBlockId = belongsBlockId;
    }
    public String getAssignedAgvCode() {
        return assignedAgvCode;
    }
    public void setAssignedAgvCode(String assignedAgvCode) {
        this.assignedAgvCode = assignedAgvCode;
    }
}
