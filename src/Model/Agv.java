package Model;

import java.util.ArrayList;

public class Agv {
	private int X;
	private int Y;
	private String podCode;
	private String agvCode;
	private String state;
	private String stopByAgvCode;
	private String stopByPodCode;
	private boolean alterable = true;
	private int runDistance = 0;
	private int runTime = 0;
	private int thisTime = 0;
	
	/**
	 * 必须要到达的点，只有到了该点才删除
	 */
	private ArrayList<Point> staticDestination = new ArrayList<>();
	/**
	 * 除了必须要到达的点，还包括换路径的出口点
	 */
	private ArrayList<Point> destination = new ArrayList<>();
	/**
	 * AGV当前执行的任务
	 */
	private ArrayList<Task> tasks;
	/**
	 * 储存AGV从开始到现在执行的任务编号
	 */
	private ArrayList<Integer> taskCodeList = new ArrayList<>();
	/**
	 * 储存AGV是否满载的信息
	 */
	private boolean isLoading = false;
	private ArrayList<Point> route = new ArrayList<>();
	
	public Agv(String agvCode1,int x,int y){
		agvCode = agvCode1;
		X = x;
		Y = y;
		state = AgvState.Free;
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
	public String getAgvCode() {
		return agvCode;
	}
	public void setAgvCode(String agvCode) {
		this.agvCode = agvCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String sta) {
		state = sta;
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
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
	public ArrayList<Point> getRoute() {
		return route;
	}
	public void setRoute(ArrayList<Point> route) {
		this.route = route;
	}
	public ArrayList<Point> getDestination() {
		return destination;
	}
	public void setDestination(ArrayList<Point> destination) {
		this.destination = destination;
	}
	public ArrayList<Point> getStaticDestination() {
		return staticDestination;
	}
	public void setStaticDestination(ArrayList<Point> staticDestination) {
		this.staticDestination = staticDestination;
	}
	public ArrayList<Task> getTasks() {
		return tasks;
	}
	public void setTasks(ArrayList<Task> task) {
		this.tasks = task;
	}
	public ArrayList<Integer> getTaskCodeList() {
		return taskCodeList;
	}
	public void setTaskCodeList(ArrayList<Integer> taskCodeList) {
		this.taskCodeList = taskCodeList;
	}
	public String getStopByAgvCode() {
		return stopByAgvCode;
	}
	public void setStopByAgvCode(String stopByAgvCode) {
		this.stopByAgvCode = stopByAgvCode;
	}
	public String getStopByPodCode() {
		return stopByPodCode;
	}
	public void setStopByPodCode(String stopByPodCode) {
		this.stopByPodCode = stopByPodCode;
	}
	public boolean isAlterable() {
		return alterable;
	}
	public void setAlterable(boolean alterable) {
		this.alterable = alterable;
	}
	public int getRunDistance() {
		return runDistance;
	}
	public void addRunDistance(){
		this.runDistance++;
	}
	public void addRunTime(){
		this.runTime++;
	}
	public void addThisTime(){
		this.thisTime++;
	}
	public int getThisTime() {
		return thisTime;
	}
	public void setThisTime(int thisTime) {
		this.thisTime = thisTime;
	}
	public String getStatisticData(){
		String s = "\n" + agvCode + "," + runDistance + "," + runTime;
		
		return s;
	}
}
