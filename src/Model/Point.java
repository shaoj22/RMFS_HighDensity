package Model;

import java.util.ArrayList;

public class Point {
	/**
	 * �ر�����λ��X
	 */
	private int X;
	/**
	 * �ر�����λ��Y
	 */
	private int Y;
	/**
	 * �ر����ڻ��������
	 */
	private int blockId;

	//�ر�����ͨ�еķ���
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	/**
	 * �ر����ͣ����������ܻ���̨��
	 */
	private String type = PointType.Undefined;
	/**
	 * �ر����Ƿ���Դ�Ż���
	 */
	private boolean isPalletPoint = false;
	/**
	 * �ر����Ƿ���С��
	 */
	private boolean occupied = false;
	/**
	 * �ر����Ƿ��л���
	 */
	private boolean occupiedByPod = false;
	/**
	 * ��λ���Ƿ��Ѿ��������ĳ����λ��
	 */
	private boolean isAssignedForPod = false;

	/**
	 * �ر��ϱ�ռ�õ�С�����
	 */
	private String occupiedAgvCode = null;
	/**
	 * �ر��ϱ�ռ�õĻ��ܱ��
	 */
	private String occupiedPodCode = null;
	
	/**
	 * �趨���潻ͨ״����ʱ��
	 */
	private int transportationTime = 5;
	/**
	 * �ر��ȥһ��ʱ���ڱ�ռ�õ�״̬
	 */
	private ArrayList<Boolean> transportationData = new ArrayList<>();
	
	/**
	 * ����õ��ر���ӵ�³̶�crowdRate�󣬸��ݹ�ʽ1/(1-crowdRate)�ɼ���С������õ������ʱ�䱶��
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
