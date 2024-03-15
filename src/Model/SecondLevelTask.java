package Model;

public class SecondLevelTask {
    private String secondLevelTaskCode = null;
    private String agvCode = null;
    private String podCode = null;
    /**
     * 任务起点数组，第一个元素为X，第二个元素为Y
     */
    private int [] origin = new int [2];
    /**
     * 任务终点数组，第一个元素为X，第二个元素为Y
     */
    private int [] destination = new int [2];

    public SecondLevelTask(int [] task){
        int [] ori = new int [2];
        int [] des = new int [2];
        ori[0] = task[0];
        ori[1] = task[1];
        des[0] = task[2];
        des[1] = task[3];
        setOrigin(ori);
        setDestination(des);
    }
    /**
     * 储存AGV接到子任务时的位置
     */
    private Point agvPoint = null;
    /**
     * 储存AGV接到任务的时间
     */
    private int recieveTaskTime = 0;
    /**
     * 储存AGV到达起点的时间
     */
    private int startTaskTime = 0;
    /**
     * 储存AGV到达终点的时间
     */
    private int endTaskTime = 0;
    /**
     * 储存AGV完成任务的时间
     */
    private int finishTaskTime = 0;
    /**
     * 储存AGV从当前点到任务起点的规划时间
     */
    private int designTimeToOrigin = 0;
    /**
     * 储存AGV从任务起点到任务终点的规划时间
     */
    private int designTimeToDestination = 0;
    /**
     * 储存AGV从任务终点返回任务起点的规划时间
     */
    private int designTimeReturnOrigin = 0;
    /**
     * 储存任务因为发现死锁而等待的时间
     */
    private int deadLockTime = 0;
    /**
     * 储存任务因为资源占用等待的时间
     */
    private int waitTime = 0;
    /**
     * 储存任务被要求换路径的次数
     */
    private int changeRouteTime = 0;
    /**
     * 判断该任务是顺路执行的任务
     */
    private boolean isByWay;



    public String getSecondLevelTaskCode() {
        return secondLevelTaskCode;
    }
    public void setSecondLevelTaskCode(String secondLevelTaskCode) {
        this.secondLevelTaskCode = secondLevelTaskCode;
    }
    public String getAgvCode() {
        return agvCode;
    }
    public void setAgvCode(String agvCode) {
        this.agvCode = agvCode;
    }
    public String getPodCode() {
        return podCode;
    }
    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }
    public int[] getOrigin() {
        return origin;
    }
    public void setOrigin(int[] origin) {
        this.origin = origin;
    }
    public int[] getDestination() {
        return destination;
    }
    public void setDestination(int[] destination) {
        this.destination = destination;
    }
    public int getOriginX(){
        return origin[0];
    }
    public int getOriginY(){
        return origin[1];
    }
    public int getDestinationX(){
        return destination[0];
    }
    public int getDestinationY(){
        return destination[1];
    }
    public boolean isByWay() {
        return isByWay;
    }
    public void setByWay(boolean byWay) {
        isByWay = byWay;
    }
    public Point getAgvPoint() {
        return agvPoint;
    }
    public void setAgvPoint(Point agvPoint) {
        this.agvPoint = agvPoint;
    }
    public int getRecieveTaskTime() {
        return recieveTaskTime;
    }
    public void setRecieveTaskTime(int recieveTaskTime) {
        this.recieveTaskTime = recieveTaskTime;
    }
    public int getStartTaskTime() {
        return startTaskTime;
    }
    public void setStartTaskTime(int startTaskTime) {
        this.startTaskTime = startTaskTime;
    }
    public int getEndTaskTime() {
        return endTaskTime;
    }
    public void setEndTaskTime(int endTaskTime) {
        this.endTaskTime = endTaskTime;
    }
    public int getFinishTaskTime() {
        return finishTaskTime;
    }
    public void setFinishTaskTime(int finishTaskTime) {
        this.finishTaskTime = finishTaskTime;
    }
    public int getDesignTimeToOrigin() {
        return designTimeToOrigin;
    }
    public void setDesignTimeToOrigin(int designTimeToOrigin) {
        this.designTimeToOrigin = designTimeToOrigin;
    }
    public int getDesignTimeToDestination() {
        return designTimeToDestination;
    }
    public void setDesignTimeToDestination(int designTimeToDestination) {
        this.designTimeToDestination = designTimeToDestination;
    }
    public int getDesignTimeReturnOrigin() {
        return designTimeReturnOrigin;
    }
    public void setDesignTimeReturnOrigin(int designTimeReturnOrigin) {
        this.designTimeReturnOrigin = designTimeReturnOrigin;
    }
    public int getDeadLockTime() {
        return deadLockTime;
    }
    public void setDeadLockTime(int deadLockTime) {
        this.deadLockTime = deadLockTime;
    }
    public int getWaitTime() {
        return waitTime;
    }
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
    public int getChangeRouteTime() {
        return changeRouteTime;
    }
    public void setChangeRouteTime(int changeRouteTime) {
        this.changeRouteTime = changeRouteTime;
    }

    public String getStatisticData(){
        String s = "\n" + secondLevelTaskCode + "," + agvCode + "," + podCode;
        if (agvPoint == null){
            s += ",";
        }else{
            s += "," + "\"" + agvPoint.getX() + "," + agvPoint.getY() + "\"";
        }
        s += "," + "\"" + origin[0] + "," + origin[1] + "\"";
        s += "," + "\"" + destination[0] + "," + destination[1] + "\"";
        s += "," + designTimeToOrigin;
        s += "," + (startTaskTime-recieveTaskTime);
        s += "," + designTimeToDestination;
        s += "," + (endTaskTime-startTaskTime);
        s += "," + designTimeReturnOrigin;
        s += "," + (finishTaskTime-endTaskTime);

        s += "," + waitTime;
        s += "," + deadLockTime;
        s += "," + changeRouteTime;

        s += "," + recieveTaskTime;
        s += "," + startTaskTime;
        s += "," + endTaskTime;
        s += "," + finishTaskTime;


        return s;
    }


}
