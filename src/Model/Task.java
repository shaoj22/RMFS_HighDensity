package Model;

import java.util.ArrayList;
import java.util.HashSet;

public class Task {
	/**
	 * 任务ID
	 */
	private int taskCode;
	/**
	 * 需要搬运的货架
	 */
	private String podCode = null;
	/**
	 * 需要搬运的货架位置
	 */
	private int [] origin = new int [2];
	/**
	 * 需要搬运到的拣选站位置
	 */
	private int [] destination = new int [2];
	/**
	 * 储存当前执行该任务的AGV集合
	 */
	private HashSet<String> agvCode = new HashSet<>();
	/**
	 * 储存由该任务生成的子任务序列
	 */
	private ArrayList<SecondLevelTask> secondLevelTasks = new ArrayList<>();
	/**
	 * 储存由该任务生成的子任务的个数
	 */
	private int secondLevelTaskNum;
	/**
	 * 判断该任务是否已经分解成子任务
	 */
	private boolean isDivided;
	/**
	 * 储存任务开始执行的时间，有AGV去执行它的子任务开始
	 */
	private int startTaskTime = 0;
	/**
	 * 存储任务完成的时间，将目标货架再放回货架区
	 */
	private int finishTaskTime = 0;
	/**
	 * 存储任务完成总时间
	 */
	private int totalCostTime = 0;
	/**
	 * Agv为了这个任务的扛货架次数
	 */
	private int carryTimes = 0;
	/**
	 * Agv为了这个任务的等待时间
	 */
	private int waitTime = 0;
	/**
	 * Agv为了这个任务发生的死锁次数
	 */
	private int deadlockTimes = 0;
	/**
	 * 判断该任务是顺路执行的任务
	 */
	private boolean isByWay = false;


	public Task(){
	}
	public Task(String [] task){
		int [] ori = new int [2];
		int [] des = new int [2];
		ori[0] = Integer.valueOf(task[0]);
		ori[1] = Integer.valueOf(task[1]);
		des[0] = Integer.valueOf(task[2]);
		des[1] = Integer.valueOf(task[3]);
		setOrigin(ori);
		setDestination(des);
	}
	public int getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(int taskCode) {
		this.taskCode = taskCode;
	}
	public void setOrigin(int [] ori){
		origin = ori.clone();
	}
	private void setDestination(int [] des){
		destination = des.clone();
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

	public String getPodCode(){
		return podCode;
	}
	public void setPodCode(String podCode){
		this.podCode = podCode;
	}
	public HashSet<String> getAgvCode() {
		return agvCode;
	}
	public void setAgvCode(HashSet<String> agvCode) {
		this.agvCode = agvCode;
	}
	public int getTotalCostTime() {
		return totalCostTime;
	}
	public void addTotalCostTime() {
		this.totalCostTime ++;
	}
	public int getCarryTimes() {
		return carryTimes;
	}
	public void addCarryTimes() {
		this.carryTimes ++;
	}
	public int getWaitTime() {
		return waitTime;
	}
	public void addWaitTime() {
		this.waitTime ++;
	}
	public int getDeadlockTimes() {
		return deadlockTimes;
	}
	public void addDeadlockTimes() {
		this.deadlockTimes ++;
	}

	public ArrayList<SecondLevelTask> getSecondLevelTasks() {
		return secondLevelTasks;
	}
	public void setSecondLevelTasks(ArrayList<SecondLevelTask> secondLevelTasks) {
		this.secondLevelTasks = secondLevelTasks;
	}
	public boolean isDivided() {
		return isDivided;
	}
	public void setDivided(boolean divided) {
		isDivided = divided;
	}
	public int getStartTaskTime() {
		return startTaskTime;
	}
	public void setStartTaskTime(int startTaskTime) {
		this.startTaskTime = startTaskTime;
	}
	public int getFinishTaskTime() {
		return finishTaskTime;
	}
	public void setFinishTaskTime(int finishTaskTime) {
		this.finishTaskTime = finishTaskTime;
	}
	public boolean isByWay() {
		return isByWay;
	}
	public void setByWay(boolean byWay) {
		isByWay = byWay;
	}
	public int getSecondLevelTaskNum() {
		return secondLevelTaskNum;
	}
	public void setSecondLevelTaskNum(int secondLevelTaskNum) {
		this.secondLevelTaskNum = secondLevelTaskNum;
	}
	public String getStatisticData(){
		String s = "\n" + taskCode ;
		if (isByWay){
			s += "," + "\"" + "Yes" + "\"";
			s += "," + "Null";
			s +=  "," + "Null";

			s += "," + "Null";
			s += "," + "Null";
			s += "," + "Null";

			s += "," + "Null";
			s += "," + "Null";
			s += "," + "Null";
		}
		else{
			s += "," + "\"" + "No" + "\"" + "," ;
			for (String agv: agvCode){
				s += " " + agv;
			}
			s +=  "," + podCode;

			s += "," + startTaskTime;
			s += "," + finishTaskTime;
			s += "," + (finishTaskTime - startTaskTime);

			s += "," + carryTimes;
			s += "," + waitTime;
			s += "," + deadlockTimes;
		}


		return s;
	}

}
