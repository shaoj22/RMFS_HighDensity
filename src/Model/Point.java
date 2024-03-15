package Model;

import java.util.ArrayList;

public class Point {
	/**
	 * 地标所在位置X
	 */
	private int X;
	/**
	 * 地标所在位置Y
	 */
	private int Y;
	/**
	 * 地标所在货架区编号
	 */
	private int blockId;

	//地标允许通行的方向
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	/**
	 * 地标类型，过道、货架或工作台等
	 */
	private String type = PointType.Undefined;
	/**
	 * 地标上是否可以存放货架
	 */
	private boolean isPalletPoint = false;
	/**
	 * 地标上是否有小车
	 */
	private boolean occupied = false;
	/**
	 * 地标上是否有货架
	 */
	private boolean occupiedByPod = false;
	/**
	 * 货位点是否已经被分配给某个货位了
	 */
	private boolean isAssignedForPod = false;

	/**
	 * 地标上被占用的小车编号
	 */
	private String occupiedAgvCode = null;
	/**
	 * 地标上被占用的货架编号
	 */
	private String occupiedPodCode = null;
	
	/**
	 * 设定储存交通状况的时长
	 */
	private int transportationTime = 5;
	/**
	 * 地标过去一段时间内被占用的状态
	 */
	private ArrayList<Boolean> transportationData = new ArrayList<>();
	
	/**
	 * 计算得到地标点的拥堵程度crowdRate后，根据公式1/(1-crowdRate)可计算小车到达该点的期望时间倍数
	 */
	private double transportationRatio = 1;





	
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
	public boolean isUp() {
		return up;
	}
	public void setUp(boolean up) {
		this.up = up;
	}
	public boolean isDown() {
		return down;
	}
	public void setDown(boolean down) {
		this.down = down;
	}
	public boolean isLeft() {
		return left;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public boolean isRight() {
		return right;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDirection(String dir)
	{
		if (dir.charAt(0) == '1')
		{
			setUp(true);
		}
		if (dir.charAt(1) == '1')
		{
			setDown(true);
		}
		if (dir.charAt(2) == '1')
		{
			setLeft(true);
		}
		if (dir.charAt(3) == '1')
		{
			setRight(true);
		}
	}
	
	public boolean isPalletPoint() {
		return isPalletPoint;
	}
	public void setPalletPoint(boolean isPalletPoint) {
		this.isPalletPoint = isPalletPoint;
	}
	public boolean isOccupied() {
		return occupied;
	}
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	public boolean isOccupiedByPod() {
		return occupiedByPod;
	}
	public boolean isAssignedForPod() {
		return isAssignedForPod;
	}
	public void setAssignedForPod(boolean assignedForPod) {
		isAssignedForPod = assignedForPod;
	}
	public void setOccupiedByPod(boolean occupied) {
		this.occupiedByPod = occupied;
	}
	public String getOccupiedAgvCode() {
		return occupiedAgvCode;
	}
	public void setOccupiedAgvCode(String occupiedAgvCode) {
		this.occupiedAgvCode = occupiedAgvCode;
	}
	public String getOccupiedPodCode() {
		return occupiedPodCode;
	}
	public void setOccupiedPodCode(String occupiedPodCode) {
		this.occupiedPodCode = occupiedPodCode;
	}
	public int getBlockId() {
		return blockId;
	}
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	public void addTransportationData(boolean occupy) {
		this.transportationData.add(occupy);
		if (this.transportationData.size()>this.transportationTime){
			this.transportationData.remove(0);
		}
	}
	public void setTransportationRatio(double transportationRatio) {
		this.transportationRatio = transportationRatio;
	}
	public void setTransportationRatio() {
		double crowdRate = 0;
		for(boolean data:this.transportationData){
			if (data){
				crowdRate++;
			}
		}
		crowdRate = crowdRate/this.transportationTime;
		if (crowdRate>0.99){
			crowdRate = 0.99;
		}
		this.transportationRatio = 1/(1-crowdRate);
	}
	public double getTransportationRatio() {
		return this.transportationRatio;
	}
}
