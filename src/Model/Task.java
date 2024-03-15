package Model;

import java.util.ArrayList;
import java.util.HashSet;

public class Task {
	/**
	 * ����ID
	 */
	private int taskCode;
	/**
	 * ��Ҫ���˵Ļ���
	 */
	private String podCode = null;
	/**
	 * ��Ҫ���˵Ļ���λ��
	 */
	private int [] origin = new int [2];
	/**
	 * ��Ҫ���˵��ļ�ѡվλ��
	 */
	private int [] destination = new int [2];
	/**
	 * ���浱ǰִ�и������AGV����
	 */
	private HashSet<String> agvCode = new HashSet<>();
	/**
	 * �����ɸ��������ɵ�����������
	 */
	private ArrayList<SecondLevelTask> secondLevelTasks = new ArrayList<>();
	/**
	 * �����ɸ��������ɵ�������ĸ���
	 */
	private int secondLevelTaskNum;
	/**
	 * �жϸ������Ƿ��Ѿ��ֽ��������
	 */
	private boolean isDivided;
	/**
	 * ��������ʼִ�е�ʱ�䣬��AGVȥִ������������ʼ
	 */
	private int startTaskTime = 0;
	/**
	 * �洢������ɵ�ʱ�䣬��Ŀ������ٷŻػ�����
	 */
	private int finishTaskTime = 0;
	/**
	 * �洢���������ʱ��
	 */
	private int totalCostTime = 0;
	/**
	 * AgvΪ���������Ŀ����ܴ���
	 */
	private int carryTimes = 0;
	/**
	 * AgvΪ���������ĵȴ�ʱ��
	 */
	private int waitTime = 0;
	/**
	 * AgvΪ���������������������
	 */
	private int deadlockTimes = 0;
	/**
	 * �жϸ�������˳·ִ�е�����
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
