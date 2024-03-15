package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import Application.WriteData;
import MultiCooperation.MultiCooperation;

public class MapInfo{
	private int rowNum;
	private int colNum;
	private int blockNum;
	private int agvNum;
	// ��¼�õ�ͼ����
	private int mapType = MapType.Undefine;
	// �洢���еر���Ϣ�������ر��ͨ�з���͵ر����͡�ֵ��ע����ǣ��洢�ľ��ε�ͼ�п�����Щ�㲻��ʹ�á�
	private Point [][] Points;
	// �洢���й���̨��
	private ArrayList<Point> WorkStations = new ArrayList<>();
	// �洢���л��ܵ���
	private ArrayList<Point> PodArea = new ArrayList<>();
	//�洢����Block��Ϣ
	private HashMap<Integer,Block> Blocks = new HashMap<>();
	//�洢���л�����Ϣ
	private HashMap<Integer,Pod> Pods = new HashMap<>();
	
	public int getMapType() {
		return mapType;
	}
	public void setMapType(int mapType) {
		this.mapType = mapType;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getColNum() {
		return colNum;
	}
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
	public Point[][] getPoints() {
		return Points;
	}
	public void setPoints(Point[][] points) {
		this.Points = points;
	}
	public ArrayList<Point> getWorkStations() {
		return WorkStations;
	}
	public void setWorkStations(ArrayList<Point> workStations) {
		WorkStations = workStations;
	}
	public ArrayList<Point> getPodArea() {
		return PodArea;
	}
	public void setPodArea(ArrayList<Point> podArea) {
		PodArea = podArea;
	}
	public int getBlockNum() {
		return blockNum;
	}
	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}
	public HashMap<Integer, Block> getBlocks() {
		return Blocks;
	}
	public void setBlocks(HashMap<Integer, Block> blocks) {
		Blocks = blocks;
	}
	public HashMap<Integer, Pod> getPods() {
		return Pods;
	}
	public void setPods(HashMap<Integer, Pod> pods) {
		Pods = pods;
	}
	public int getAgvNum() {
		return agvNum;
	}
	public void setAgvNum(int agvNum) {
		this.agvNum = agvNum;
	}

	/**
	 * ���������ѿ�������
	 * @param start
	 * @param end
	 * @return
	 */
	public double getEstimatedDistance(Point start, Point end){
		return 0.9*(Math.abs(start.getX()-end.getX())+Math.abs(start.getY()-end.getY()));
	}
	
	/**
	 * �ж��������ǲ���ͬһ��
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isSamePoint(Point a, Point b){
		if (a == null || b == null){
			return false;
		}
		if (a.getX() == b.getX() && a.getY() == b.getY()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * �ж�һ������û��·��
	 */
	public boolean isUsefulPoint(Point p){
		if (p.getType().equals(PointType.Pillar)||p.getType().equals(PointType.Undefined)){
			return false;
		}
		return true;
	}
	
	/**
	 * �ж�һ�����ܲ�����Ϊ�������
	 */
	public boolean isStartPoint(Point p){
		if (p.getType().equals(PointType.Pallet)||p.getType().equals(PointType.Buffer)){
			return true;
		}
		return false;
	}

	/**
	 * �ж�һ�����Ƿ��ǻ������߽��
	 */
	public boolean isBoundry(Point p){
		Point pUp = Points[p.getX() - 1][p.getY()];
		Point pDown = Points[p.getX() + 1][p.getY()];
		Point pLeft = Points[p.getX()][p.getY() - 1];
		Point pRight = Points[p.getX()][p.getY() + 1];
		if (!pUp.getType().equals(PointType.Pallet)){
			return true;
		}
		if (!pDown.getType().equals(PointType.Pallet)){
			return true;
		}
		if (!pLeft.getType().equals(PointType.Pallet)){
			return true;
		}
		if (!pRight.getType().equals(PointType.Pallet)){
			return true;
		}
		return false;
	}

	/**
	 * �ж�һ�����Ƿ��ǻ����������
	 */
	public boolean isCorner(Point p){
		int count = 0;
		if (p.getX() == 0 || p.getX() == rowNum - 1 || p.getY() == 0|| p.getY() == colNum - 1){
			if ((p.getX() == 0 || p.getX() == rowNum - 1) && (p.getY() == 0 || p.getY() == colNum - 1)){
				return true;
			}
			else if (p.getX() == 0 || p.getX() == rowNum - 1){
				Point pLeft = Points[p.getX()][p.getY() - 1];
				Point pRight = Points[p.getX()][p.getY() + 1];
				if (!pLeft.getType().equals(pRight.getType())){
					return true;
				}
				else{
					return false;
				}
			}
			else if (p.getY() == 0|| p.getY() == colNum - 1){
				Point pUp = Points[p.getX() - 1][p.getY()];
				Point pDown = Points[p.getX() + 1][p.getY()];
				if (!pUp.getType().equals(pDown.getType())){
					return true;
				}
				else{
					return false;
				}
			}
			else {
				return false;
			}
		}
		else{
			Point pUp = Points[p.getX() - 1][p.getY()];
			Point pDown = Points[p.getX() + 1][p.getY()];
			Point pLeft = Points[p.getX()][p.getY() - 1];
			Point pRight = Points[p.getX()][p.getY() + 1];
			if (!pUp.getType().equals(PointType.Pallet)){
				count ++;
			}
			if (!pDown.getType().equals(PointType.Pallet)){
				count ++;
			}
			if (!pLeft.getType().equals(PointType.Pallet)){
				count ++;
			}
			if (!pRight.getType().equals(PointType.Pallet)){
				count ++;
			}
			if (count == 2){
				return true;
			}
			else{
				return false;
			}
		}
	}

	/**
	 * �ж�һ�����Ƿ��ǻ��������Ͻ�
	 */
	public boolean isLeftUpCorner(Point p){
		if ((p.getY() == 0 || (!Points[p.getX()][p.getY() - 1].getType().equals(PointType.Pallet)))
			&& (p.getX() == 0 || (!Points[p.getX() - 1][p.getY()].getType().equals(PointType.Pallet)))){
			return true;
		}
		return false;
	}
	/**
	 * �ж�һ�����ʱ�Ƿ��ı߶��л���
	 */
	public boolean isBlocked(Point p,Point d){
		int dis = Astar(p,d,true).getRoute().size();
		if (dis < 1) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * ��������ܰ�����С�ķ���
	 */
	public int calcDirction (Point p){
		Block block = getBlocks().get(p.getBlockId());
		int up = 0;
		int down = 0;
		int left = 0;
		int right = 0;
		for (int i = block.getBlockStart().getX(); i < p.getX(); i++){
			if (Points[i][p.getY()].isOccupiedByPod()){
				up ++;
			}
		}
		for (int i = 1; i + p.getX() < block.getBlockStart().getX() + block.getRowNum(); i++){
			if (Points[p.getX() + i][p.getY()].isOccupiedByPod()){
				down ++;
			}
		}
		for (int i = block.getBlockStart().getY(); i < p.getY(); i++){
			if (Points[p.getX()][i].isOccupiedByPod()){
				left ++;
			}
		}
		for (int i = 1; i + p.getY() < block.getBlockStart().getY() + block.getColNum(); i++){
			if (Points[p.getX()][p.getY() + i].isOccupiedByPod()){
				right ++;
			}
		}
		if (block.getBlockStart().getX() == 0){
			up = Integer.MAX_VALUE;
		}
		else if (block.getBlockStart().getX() + block.getRowNum() - 1  == rowNum - 1){
			down = Integer.MAX_VALUE;
		}
		if (block.getBlockStart().getY() == 0){
			left = Integer.MAX_VALUE;
		}
		else if (block.getBlockStart().getY() + block.getColNum() - 1  == colNum - 1){
			right = Integer.MAX_VALUE;
		}
		int minDirection = 0;
		int minStep = up;
		ArrayList<Integer> step = new ArrayList<>();
		step.add(up);
		step.add(down);
		step.add(left);
		step.add(right);
		int[] Xdir = {-1, 1, 0, 0};
		int[] Ydir = {0, 0, -1, 1};
		int[] dis = {Math.abs(p.getX() - block.getBlockStart().getX()), Math.abs(block.getBlockStart().getX() + rowNum - 1 - p.getX())
				,Math.abs(p.getY() - block.getBlockStart().getY()), Math.abs(block.getBlockStart().getY() + colNum - 1 - p.getY())};
		for (int i = 1; i < 4; i++){
			if (step.get(i) < minStep){
				minStep = step.get(i);
				minDirection = i;
			}
			//�������������Ҫ���˵Ļ���������ͬ��ѡһ����߽������
			else if (step.get(i) == minStep){
				if (dis[i] < dis[minDirection]){
					minStep = step.get(i);
					minDirection = i;
				}
			}
		}
		return minDirection;
	}
	/**
	 * ���ݷ���������Ҫ���˵Ļ������У�*���Ϊ0*����Ҫ���Ȱ��
	 */
	public ArrayList<Pod> needMove (Point p,int direction){
		ArrayList<Pod> pods = new ArrayList<>();
		Block block = getBlocks().get(p.getBlockId());
		if (direction == 0){
			for (int i = block.getBlockStart().getX(); i < p.getX(); i++){
				if (Points[i][p.getY()].isOccupiedByPod()
						&& !getPods().get(Integer.valueOf(Points[i][p.getY()].getOccupiedPodCode())).isAssigned()){
					Pod tmp = getPods().get(Integer.valueOf(Points[i][p.getY()].getOccupiedPodCode()));
					pods.add(tmp);
				}
			}
		}
		else if (direction == 1){
			for (int i = block.getBlockStart().getX() + block.getRowNum() - 1; p.getX() < i; i--){
				if (Points[i][p.getY()].isOccupiedByPod()
						&& !getPods().get(Integer.valueOf(Points[i][p.getY()].getOccupiedPodCode())).isAssigned()){
					Pod tmp = getPods().get(Integer.valueOf(Points[i][p.getY()].getOccupiedPodCode()));
					pods.add(tmp);
				}
			}
		}
		else if (direction == 2){
			for (int i = block.getBlockStart().getY(); i < p.getY(); i++){
				if (Points[p.getX()][i].isOccupiedByPod()
						&& !getPods().get(Integer.valueOf(Points[p.getX()][i].getOccupiedPodCode())).isAssigned()){
					Pod tmp = getPods().get(Integer.valueOf(Points[p.getX()][i].getOccupiedPodCode()));
					pods.add(tmp);
				}
			}
		}
		else{
			for (int i = block.getBlockStart().getY() + block.getColNum() - 1; p.getY() < i; i--){
				if (Points[p.getX()][i].isOccupiedByPod()
						&& !getPods().get(Integer.valueOf(Points[p.getX()][i].getOccupiedPodCode())).isAssigned()){
					Pod tmp = getPods().get(Integer.valueOf(Points[p.getX()][i].getOccupiedPodCode()));
					pods.add(tmp);
				}
			}
		}
		return pods;
	}


	/**
	 * �������������������δ������ĵ㣬����������ܵ��յ�
	 * ���ܵ�����λ��������
	 * 1.�û�λ���Ѿ����������Ļ���
	 * 2.�û�λ����Ŀ����ܳ�ȥ��·����
	 * ���̣�
	 * ���ж��Լ���ǰ������block�У�������Ŀ����ܵ�λ�ã��ܹ�ֱ��ͨ���ⲿ�ƶ����ģ������ĵ������λ������û�пջ�λ
	 * ���жϱ��block����ֱ��ͨ���ⲿ�����block*���ĵ�*����������Լ������δ������Ŀջ�λ
	 */
	public Point findOutsidePoint (Point originPod, Pod pod, Block b,int direction){
		Point newLocation = null;
		double disToCenter = Double.MAX_VALUE;
		ArrayList<Point> emptyPodLocation= b.getEmptyPodLocation();
		for (Point p : emptyPodLocation){
			//δ���������Ļ���
			if (!p.isAssignedForPod()){
				//������Ŀ����ܳ�ȥ��·����
				if ((direction == 0 && (p.getY() != originPod.getY() || p.getX() > originPod.getX()))
						|| (direction == 1 && (p.getY() != originPod.getY() || p.getX() < originPod.getX()))
						|| (direction == 2 && (p.getX() != originPod.getX() || p.getY() > originPod.getY()))
						|| (direction == 3 && (p.getX() != originPod.getX() || p.getY() < originPod.getY()))){
					Double dis = Astar(Points[pod.getX()][pod.getY()],p,true).getDistance();
					//�ܹ�ֱ�ӵ���Ŀջ�λ��
					if (dis != 0){
						Double tempdis = Math.abs(p.getX() - (b.getBlockStart().getX() + b.getRowNum()/2.0)) + Math.abs(p.getY() - (b.getBlockStart().getY() + b.getColNum()/2.0));
						//ѡ�������ĵ�����Ŀջ���
						if (tempdis < disToCenter){
							disToCenter = tempdis;
							newLocation = p;
						}
					}
				}
			}
		}
		return newLocation;
	}
	/**
	 * �������������������δ������ĵ㣬����������ܵ��յ�
	 * ���ܵ�����λ��������
	 * 1.�û�λ���Ѿ����������Ļ���
	 * 2.�û�λ����Ŀ����ܳ�ȥ��·����
	 * ���û�п���ֱ�ӵ���Ļ��ܵ㣬��������Ļ��ܵ�
	 */

	public Point findStayLocation (Point originalPod, Pod pod, int direction){
		//map 2 agv 5
		int mostEmpty = 0;
		for (int i = 0; i < blockNum;i++){
			if (getBlocks().get(i).getEmptyPodLocation().size() > getBlocks().get(mostEmpty).getEmptyPodLocation().size()){
				mostEmpty = i;
			}
		}

		Block b = getBlocks().get(mostEmpty);
		Point newLocation = findOutsidePoint(originalPod,pod,b,direction);
		if (newLocation != null){
			return newLocation;
		}
		//map 2 agv 5
		b = getBlocks().get(pod.getBelongsBlockId());
		newLocation = findOutsidePoint(originalPod,pod,b,direction);
		if (newLocation != null){
			return newLocation;
		}

		double dis = 0;
		for (int i = 0; i < blockNum; i++){
			b = getBlocks().get(i);
			if (i != pod.getBelongsBlockId()){
				Point tempLocation = findOutsidePoint(originalPod,pod,b,direction);
				if (tempLocation != null){
					if (Astar(Points[pod.getX()][pod.getY()],tempLocation,true).getDistance() > dis){
						dis = Astar(Points[pod.getX()][pod.getY()],tempLocation,true).getDistance();
						newLocation = tempLocation;
					}
				}
			}
		}
		if (newLocation != null){
			return newLocation;
		}
		//���û��ֱ�ӿ��Ե���Ļ��ܵ㣬������Ļ����о��뵱ǰ���ܹ�������������Ŀջ�λ��
		for (int i = 0; i < blockNum; i++) {
			b = getBlocks().get(i);
			ArrayList<Point> emptyLocation = b.getEmptyPodLocation();
			for (Point p : emptyLocation) {
				//δ���������Ļ���
				if (!p.isAssignedForPod()){
					//������Ŀ����ܳ�ȥ��·����
					if ((direction == 0 && (p.getY() != pod.getY() || p.getX() > pod.getX()))
							|| (direction == 1 && (p.getY() != pod.getY() || p.getX() < pod.getX()))
							|| (direction == 2 && (p.getX() != pod.getX() || p.getY() > pod.getY()))
							|| (direction == 3 && (p.getX() != pod.getX() || p.getY() < pod.getY()))) {
						int tmpdis = Math.abs(pod.getX() - p.getX()) + Math.abs(pod.getY() - p.getY());
						if (tmpdis > dis){
							dis = tmpdis;
							newLocation = p;
						}
					}
				}
			}
		}

		return newLocation;
	}

	public Point findStayLocationByPoint(Point point){
		Block b = getBlocks().get(point.getBlockId());
		ArrayList<Point> emptyPodLocation= b.getEmptyPodLocation();
		for (Point p : emptyPodLocation) {
			if (!p.isAssignedForPod()){
				return p;
			}
		}
		for (int i = 0; i < blockNum; i++){
			b = getBlocks().get(i);
			emptyPodLocation= b.getEmptyPodLocation();
			if (i != point.getBlockId()){
				for (Point p : emptyPodLocation) {
					if (!p.isAssignedForPod()){
						return p;
					}
				}
			}
		}

		return null;
	}


	/**
	 * ����ڵ�from��to֮���Ƿ����ͨ�У���Ҫ�����������
	 * @param init
	 * @param from
	 * @param to
	 * @param isLoading
	 * @return
	 */
	public boolean checkMovable(Point init, Point from, Point to, boolean isLoading){
		if (isLoading){
			// ��������£�������AGV�ӻ��ܵ��ƶ������صĻ��ܵ�
			if (PointType.Pallet.equals(to.getType()) && to.isOccupiedByPod()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * �õ������ڵ�
	 */
	public ArrayList<Point> getNeighborPoints(Point currentPoint, int direction){
		ArrayList<Point> neighborList = new ArrayList<>();


		if (direction == 0){
			if (currentPoint.isUp()){//X-1
				neighborList.add(Points[currentPoint.getX()-1][currentPoint.getY()]);
			}
			if (currentPoint.isDown()){//X+1
				neighborList.add(Points[currentPoint.getX()+1][currentPoint.getY()]);
			}
			if (currentPoint.isLeft()){//Y-1
				neighborList.add(Points[currentPoint.getX()][currentPoint.getY()-1]);
			}
			if (currentPoint.isRight()){//Y+1
				neighborList.add(Points[currentPoint.getX()][currentPoint.getY()+1]);
			}
		}
		else {
			if (currentPoint.isLeft()){//Y-1
				neighborList.add(Points[currentPoint.getX()][currentPoint.getY()-1]);
			}
			if (currentPoint.isRight()){//Y+1
				neighborList.add(Points[currentPoint.getX()][currentPoint.getY()+1]);
			}
			if (currentPoint.isUp()){//X-1
				neighborList.add(Points[currentPoint.getX()-1][currentPoint.getY()]);
			}
			if (currentPoint.isDown()){//X+1
				neighborList.add(Points[currentPoint.getX()+1][currentPoint.getY()]);
			}
		}

		//map1
		if (getPods().size() == 320 && agvNum == 10){
			Random rnd = new Random(2);
			Collections.shuffle(neighborList,rnd);
		}/*
		if (getPods().size() == 320 && agvNum == 6){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && agvNum == 7){
			Random rnd = new Random(2);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && agvNum == 8){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && agvNum == 9){
			Random rnd = new Random(5);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 11 || agvNum == 12 || agvNum == 13 || agvNum == 14 || agvNum == 15)){
			Random rnd = new Random(9);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);

		}
		if (getPods().size() == 320 && (agvNum == 15)){
			Random rnd = new Random();
			Collections.shuffle(neighborList,rnd);

		}
		if (getPods().size() == 320 && (agvNum == 16)){
			Random rnd = new Random();
			Collections.shuffle(neighborList,rnd);
		}*/
		/*if (getPods().size() == 320 && (agvNum >= 15) && agvNum != 18){
			Random rnd = new Random();
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum >= 15) && agvNum == 18){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}*/

		//map2
		/*if (getPods().size() == 320 && (agvNum == 13)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && agvNum == 14){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && agvNum >= 17){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }

        *//*if (getPods().size() == 323 && agvNum == 10){  //task == 1400
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }*//*
        if (getPods().size() == 323 && (agvNum == 15 || agvNum == 18 || agvNum == 19)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 323 && agvNum == 20){
            Random rnd = new Random(5);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 342 && (agvNum >= 15 )){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 349 && (agvNum == 12 || agvNum >= 14)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 361 && ( agvNum >= 14)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }*/

        //map 3
       /* if (getPods().size() == 320 && ( agvNum == 11 || agvNum >= 14) && agvNum != 20 && agvNum != 15){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 320 && agvNum == 15){
            Random rnd = new Random(2);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 320 && ( agvNum >= 18)){
            Random rnd = new Random();
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 340 && ( agvNum == 8)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 340 && ( agvNum >= 14)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 340 && ( agvNum >= 17)){
            Random rnd = new Random();
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 360 && ( agvNum == 6)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 360 && ( agvNum == 14)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 360 && ( agvNum >= 15) && agvNum != 20){
            Random rnd = new Random();
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 360 && ( agvNum == 20)){
            Random rnd = new Random();
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 368 && ( agvNum >=10 && agvNum < 14)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 368 && (  agvNum >= 6 && agvNum < 10)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 368 && ( agvNum == 14 || agvNum == 15)){
            Random rnd = new Random(3);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 368 && ( agvNum >= 16)){
            Random rnd = new Random();
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 380 && ( agvNum == 3 || agvNum == 5 || agvNum == 7 || agvNum == 12 || agvNum == 13)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 380 && (agvNum == 14)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 380 && (agvNum == 17)){
            Random rnd = new Random(4);
            Collections.shuffle(neighborList,rnd);
            rnd = new Random(8);
            Collections.shuffle(neighborList,rnd);
        }
        if (getPods().size() == 380 && (agvNum >= 18)){
            Random rnd = new Random();
            Collections.shuffle(neighborList,rnd);
        }*/

       //map4
       /* if (getPods().size() == 320 && (agvNum == 10)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 4)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 11)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 13)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 16)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 17)){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 18)){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 19)){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 320 && (agvNum == 20)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 352 && (agvNum == 10)){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 352 && (agvNum == 9)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 352 && (agvNum == 18)){
			Random rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 374 && (agvNum == 10)){
			Random rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 374 && (agvNum == 9 || agvNum == 11 || agvNum >= 14)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 396 && (agvNum == 10)){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 396 && (agvNum >= 5) && agvNum != 10 && agvNum != 18){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 396 && agvNum == 18){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 405 && agvNum == 10){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 405 && agvNum >= 11){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 10){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 7 || agvNum == 8 || agvNum == 11){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 13){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 14){
			Random rnd = new Random(3);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(7);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 15){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 17){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}
		if (getPods().size() == 418 && agvNum == 18){
			Random rnd = new Random(4);
			Collections.shuffle(neighborList,rnd);
			rnd = new Random(8);
			Collections.shuffle(neighborList,rnd);
		}*/

        return neighborList;
	}
	
	/**
	 * �ҳ���
	 * ���жϸ�AGV�����ǲ��Ƕ�blockס�ˣ�����ǵĻ�����ô����avoidPoints.get(0)
	 */
	public Point getOutPoint(boolean isLoading, ArrayList<Point> avoidPoints,Point currentPoint,Point nextPoint){
		Point outPoint = null;
		Point curPoint = currentPoint;
		// �ҵ�currentPoint��avoidPoints�е�λ��
		if (isLoading && isBlocked(currentPoint,getWorkStations().get(0))
				&& nextPoint != null && currentPoint.getType().equals(PointType.Pallet)){
			outPoint = nextPoint;
			return outPoint;
		}

		int index = -1;
		for (int i = 0; i<avoidPoints.size(); i++){
			if (currentPoint.equals(avoidPoints.get(i))){
				index = i;
				break;
			}
		}
		int direction = 0; //���������ҷ���

		if (avoidPoints.get(0).getX() == currentPoint.getX()){
			direction = 0;
		}
		else {
			direction = 1;
		}
		
		// �ҳ��ڵ�
		while (outPoint == null){
			// ȡ���������ڵ㼰����
			ArrayList<Point> neighborList = getNeighborPoints(curPoint,direction);
			boolean hasFind = false;
			
			for (Point p:neighborList){
				if (isLoading && (p.getType().equals(PointType.Pallet) && (p.isAssignedForPod() || p.isOccupiedByPod()))){
					continue;
				}
				if (!avoidPoints.contains(p)&&!p.equals(nextPoint)){
					outPoint = p;
					hasFind = true;
					break;
				}
			}
			
			if (!hasFind){
				index++;
			}
			// �Ҳ�����һ����
			if (index >= avoidPoints.size()){
				break;
			}
			if (index != -1) {
				curPoint = avoidPoints.get(index);
			}
		}
		if (outPoint == null){
			outPoint = avoidPoints.get(0);
		}
		
		return outPoint;
	}
	
	/**
	 * ����route�õ�Agvǰ����ÿһ���㣬�����Լ���
	 * @param result
	 * @param route
	 * @param end
	 */
	public void getRoute(ArrayList<Point> result, int [][]route, Point end){
		int currentX = end.getX();
		int currentY = end.getY();
		try{
			while (route[currentX][currentY] != -5){
				result.add(0, Points[currentX][currentY]);
				if (route[currentX][currentY] == DirectionType.UP){
					currentX--;
				}else if (route[currentX][currentY] == DirectionType.DOWN){
					currentX++;
				}else if (route[currentX][currentY] == DirectionType.LEFT){
					currentY--;
				}else {
					currentY++;
				}
			}
			result.add(0, Points[currentX][currentY]);
		}catch(Exception e){
			result.clear();
			System.out.println(String.format("Exception has happened in function <getRoute>:[%s]", e.toString()));
		}
	}

	public int designPath (){
		//map 1
		/*if (getPods().size() == 320 && agvNum == 11){
			return 4;
		}
		if (getPods().size() == 320 && (agvNum == 13||agvNum == 14)){
			Random rnd = new Random(9);
			return rnd.nextInt(5) + 1;
		}
		if (getPods().size() == 320 && (agvNum == 15)){
			Random rnd = new Random(7);
			return rnd.nextInt(5) + 1;
		}*/
		/*if (getPods().size() == 320 && (agvNum >= 15) ){
			return 4;
		}*/

		//map3
       /* if (getPods().size() == 320 && ( agvNum == 19)){
            return 4;
        }
        if (getPods().size() == 320 && ( agvNum == 11 || agvNum >= 16) && agvNum != 20){
            return 3;
        }
        if (getPods().size() == 320 && ( agvNum == 20)){
            Random rnd = new Random();
            return rnd.nextInt(5) + 1;
        }
        if (getPods().size() == 340 && ( agvNum >= 17)){
            Random rnd = new Random(7);
            return rnd.nextInt(5) + 1;
        }
        if (getPods().size() == 340 && ( agvNum == 14 || agvNum == 16)){
            return 4;
        }
        if (getPods().size() == 360 && ( agvNum == 20)){
            Random rnd = new Random();
            return rnd.nextInt(3) + 2;
        }
        if (getPods().size() == 368 && ( agvNum == 10)){
            return 3;
        }
        if (getPods().size() == 368 && ( agvNum >= 6 && agvNum < 10)){
            return 3;
        }
        if (getPods().size() == 368 && ( agvNum >= 11 && agvNum < 13)){
            return 4;
        }
        if (getPods().size() == 368 && ( agvNum == 14)){
            return 3;
        }
        if (getPods().size() == 368 && ( agvNum == 15 || agvNum == 16)){
            return 4;
        }
        if (getPods().size() == 368 && agvNum == 18){
            return 2;
        }
        if (getPods().size() == 368 && agvNum == 19){
            return 3;
        }
        if (getPods().size() == 368 && agvNum == 20){
            return 4;
        }
        if (getPods().size() == 380 && (agvNum == 12 || agvNum == 13 || agvNum == 14)){
            return 4;
        }
        if (getPods().size() == 380 && ( agvNum == 14)){
            return 3;
        }
        if (getPods().size() == 380 && ( agvNum == 15)){
            return 3;
        }
        if (getPods().size() == 380 && ( agvNum == 17)){
            return 3;
        }
        if (getPods().size() == 380 && ( agvNum == 18)){
            return 4;
        }
        if (getPods().size() == 380 && ( agvNum == 19)){
            Random rnd = new Random();
            return rnd.nextInt(5) + 1;
        }*/
		//map4
		/*if (getPods().size() == 320 && (agvNum == 10)){
			return 4;
		}
		if (getPods().size() == 320 && (agvNum == 11)){
			return 4;
		}
		if (getPods().size() == 320 && (agvNum == 13)){
			return 4;
		}
		if (getPods().size() == 320 && (agvNum == 16)){
			return 3;
		}
		if (getPods().size() == 320 && (agvNum == 17)){
			return 4;
		}
		if (getPods().size() == 320 && (agvNum == 18)){
			return 4;
		}
		if (getPods().size() == 320 && (agvNum == 19)){
			return 3;
		}
		if (getPods().size() == 320 && (agvNum == 19)){
			return 3;
		}
		if (getPods().size() == 352 && (agvNum == 10)){
			return 3;
		}
		if (getPods().size() == 352 && (agvNum == 9)){
			return 4;
		}
		if (getPods().size() == 352 && (agvNum == 13)){
			return 4;
		}
		if (getPods().size() == 352 && (agvNum == 14)){
			return 4;
		}
		if (getPods().size() == 352 && (agvNum == 15)){
			return 4;
		}
		if (getPods().size() == 352 && (agvNum == 17)){
			return 3;
		}
		if (getPods().size() == 352 && (agvNum == 18)){
			return 4;
		}
		if (getPods().size() == 374 && (agvNum == 10)){
			return 4;
		}
		if (getPods().size() == 374 && (agvNum == 4 || agvNum == 7 || agvNum == 8)){
			return 4;
		}
		if (getPods().size() == 374 && (agvNum == 9 || agvNum == 11 )){
			return 4;
		}
		if (getPods().size() == 374 && (agvNum == 15)){
			return 3;
		}
		if (getPods().size() == 374 && (agvNum == 18)){
			return 3;
		}
		if (getPods().size() == 396 && (agvNum == 10)){
			return 4;
		}
		if (getPods().size() == 396 && (agvNum == 11)){
			return 4;
		}
		if (getPods().size() == 396 && (agvNum == 18)){
			return 4;
		}
		if (getPods().size() == 396 && (agvNum >= 16)){
			return 4;
		}
		if (getPods().size() == 405 && (agvNum == 10)){
			return 4;
		}
		if (getPods().size() == 405 && (agvNum == 9)){
			return 4;
		}
		if (getPods().size() == 405 && (agvNum >= 11 && agvNum < 17)){
			return 4;
		}
		if (getPods().size() == 405 && (agvNum >= 17)){
			return 3;
		}
		if (getPods().size() == 418 && agvNum == 10){
			return 3;
		}
		if (getPods().size() == 418 && agvNum == 8){
			return 3;
		}
		if (getPods().size() == 418 && agvNum == 11){
			return 3;
		}
		if (getPods().size() == 418 && agvNum == 13){
			return 4;
		}
		if (getPods().size() == 418 && agvNum == 14){
			return 4;
		}
		if (getPods().size() == 418 && agvNum == 15){
			return 4;
		}
		if (getPods().size() == 418 && agvNum == 16){
			return 4;
		}
		if (getPods().size() == 418 && agvNum == 17){
			return 4;
		}
		if (getPods().size() == 418 && agvNum == 18){
			return 4;
		}*/




		return 1;
	}

	
	public RouteResult Astar(Point start, Point end, boolean isLoading){
		RouteResult routeResult = new RouteResult();
		// ��¼����㵽���е��ʵ�ʾ���
		double [][] distanceFromStart = new double [rowNum][colNum];
		// ��¼�����е㵽�յ�Ĺ������
		double [][] distanceToEnd = new double [rowNum][colNum];
		int [][] route = new int [rowNum][colNum];
		ArrayList<Point> currentList = new ArrayList<>();
		//��ʼ��
		for (int i = 0; i < rowNum; i++){
			for (int j = 0; j < colNum; j++){
				distanceFromStart[i][j] = Double.MAX_VALUE;
				distanceToEnd[i][j] = Double.MAX_VALUE;
				route[i][j] = Integer.MAX_VALUE;
			}
		}
		// ������������б���
		currentList.add(start);
		distanceFromStart[start.getX()][start.getY()] = 0;
		distanceToEnd[start.getX()][start.getY()] = getEstimatedDistance(start,end);
		route[start.getX()][start.getY()] = -5;
		
		// ȡ������ж��ڵ����������һ�����������,randȡ0�������ң�������������
		Random random = new Random();
		int rand = random.nextInt(2);
		int offset = 0;
		while(!currentList.isEmpty()){
			// �ڱ����б��������ܾ�����С�ĵ�
			int index = -1;
			double minDistance = Double.MAX_VALUE;
			for(int i = 0; i < currentList.size(); i++){
				Point point = currentList.get(i);
				double totalDistance = distanceFromStart[point.getX()][point.getY()]+distanceToEnd[point.getX()][point.getY()];
				if (totalDistance<minDistance){
					minDistance = totalDistance;
					index = i;
				}
			}
			Point currentPoint = currentList.get(index);
			currentList.remove(index);
			
			if (isSamePoint(currentPoint, end)){
				break;
			}
			
			// ȡ���������ڵ㼰����,��������Ұ��������һ��������µ�˳��
			ArrayList<Point> neighborList = new ArrayList<>();
			ArrayList<Integer> neighborDirection = new ArrayList<>();

			if (currentPoint.isUp()){//X-1
				neighborList.add(Points[currentPoint.getX()-1][currentPoint.getY()]);
				neighborDirection.add(DirectionType.UP);
			}

			if (currentPoint.isDown()){//X+1
				neighborList.add(Points[currentPoint.getX()+1][currentPoint.getY()]);
				neighborDirection.add(DirectionType.DOWN);
			}

			if (currentPoint.isLeft()){//Y-1
				neighborList.add(Points[currentPoint.getX()][currentPoint.getY()-1]);
				neighborDirection.add(DirectionType.LEFT);
				offset ++;
			}

			if (currentPoint.isRight()){//Y+1
				neighborList.add(Points[currentPoint.getX()][currentPoint.getY()+1]);
				neighborDirection.add(DirectionType.RIGHT);
			}




			// ����rand�趨offset��ʵ���ڵ�˳��
			/*if (rand == 1) {
				Collections.reverse(neighborList);
				Collections.reverse(neighborDirection);
			}*/
			// ���������ڵ㣬���Խ�������б���
			for (int i = 0; i < neighborList.size(); i++){
				Point neighborPoint = neighborList.get(i);
				if (checkMovable(start,currentPoint,neighborPoint,isLoading)){
					int x = neighborPoint.getX();
					int y = neighborPoint.getY();
					// �״μ����б����Ҫ�����䵽�յ�ľ��룬�������б�
					if (distanceToEnd[x][y] == Double.MAX_VALUE){
						distanceToEnd[x][y] = getEstimatedDistance(neighborPoint,end);
						currentList.add(0,neighborPoint);
					}
					// ���ȴӻ��ܵ��·�����
					double ratio = 1;
					int situation = designPath();

					if (situation == 1){
						if (neighborPoint.isPalletPoint() && currentPoint.isPalletPoint() && !neighborPoint.isOccupied()){
							ratio = 0.9;
						}
					}
					else if (situation == 2){
						if (neighborPoint.isPalletPoint() && currentPoint.isPalletPoint() ){
							ratio = 0.9;
						}
					}
					else if (situation == 3){
						if (isLoading && neighborPoint.isPalletPoint() && currentPoint.isPalletPoint() && !neighborPoint.isOccupied()){
							ratio = 0.9;
						}
					}
					else if (situation == 4){
						if (isLoading && neighborPoint.isPalletPoint() && currentPoint.isPalletPoint()){
							ratio = 0.9;
						}
					}



					// ���¾��룺����ƽ����
					double newDistance = distanceFromStart[currentPoint.getX()][currentPoint.getY()]+Math.sqrt(currentPoint.getTransportationRatio()*neighborPoint.getTransportationRatio())*ratio;
					
					if (newDistance < distanceFromStart[x][y]){
						distanceFromStart[x][y] = newDistance;
						route[x][y] = -neighborDirection.get(i);
					}
				}
			}
		}

		routeResult.setDistance(distanceFromStart[end.getX()][end.getY()]);
		if (routeResult.getDistance() < Double.MAX_VALUE) {
			getRoute(routeResult.getRoute(), route, end);

			//���ǼӼ��١�ת�䡢��ѡ�Ϳ���ʱ��
            ArrayList<Point> considerTurn = new ArrayList<>();
            for (int i = 1; i < routeResult.getRoute().size() - 1; i++){
                if ((routeResult.getRoute().get(i - 1).getX() != routeResult.getRoute().get(i + 1).getX()) && (routeResult.getRoute().get(i - 1).getY() != routeResult.getRoute().get(i + 1).getY())){
                    //ת��
                	considerTurn.add(routeResult.getRoute().get(i));
                    considerTurn.add(routeResult.getRoute().get(i));
                    considerTurn.add(routeResult.getRoute().get(i));
                }
                else{
                    considerTurn.add(routeResult.getRoute().get(i));
                }
            }
            //����յ�Ӽ���
            considerTurn.add(0,routeResult.getRoute().get(0));
            considerTurn.add(0,routeResult.getRoute().get(0));
            considerTurn.add(routeResult.getRoute().get(routeResult.getRoute().size() - 1));
            considerTurn.add(routeResult.getRoute().get(routeResult.getRoute().size() - 1));
            if (end.getType().equals(PointType.WorkStation)){
            	for (int pickTime = 0; pickTime < 10; pickTime ++){  //��ѡʱ��
					considerTurn.add(routeResult.getRoute().get(routeResult.getRoute().size() - 1));
				}
            }
            if (end.getType().equals(PointType.Pallet) && isLoading == false){
            	for (int time = 0; time < 3; time ++) {  //����ʱ��
					considerTurn.add(routeResult.getRoute().get(routeResult.getRoute().size() - 1));
				}
            }
            if (end.getType().equals(PointType.Pallet) && start.getType().equals(PointType.WorkStation)){
				for (int time = 0; time < 3; time ++) {  //����ʱ��
					considerTurn.add(routeResult.getRoute().get(routeResult.getRoute().size() - 1));
				}
            }
            routeResult.setRoute(considerTurn);
		}

		
		return routeResult;
	}

	public void generatePod(int podNum, String podLocation){
		int count = 0;
		ArrayList<Point> Location = new ArrayList<>();
		for (int i = 0; i < Points.length; i++){
			for (int j = 0; j < Points[0].length; j++){
				if (Points[i][j].getType().equals(PointType.Pallet)) {
					Location.add(Points[i][j]);
				}
			}
		}
		System.out.println(Location.size());
		String tempString = String.valueOf(podNum) + "\r\n";
		if (MapType.WareHouse == mapType) {
			while (count < podNum) {
				Random random = new Random();
				int m = random.nextInt(Location.size());
				Point location = Location.get(m);
				tempString += "\r\n" + (count + 1) + " " + location.getX() + " " + location.getY();
				Location.remove(m);
				count++;
			}
		}
		WriteData.writeData(podLocation, tempString);
	}
	
	public void generateTask(int taskNum, String taskLocation){
		String tempString = String.valueOf(taskNum)+"\r\n";
		int count = 0;
		if (MapType.WareHouse == mapType){
			// �����ͼ�����ǲֿ⣬ֻ�����������Ϊ���ܵ㣬�����յ�Ϊ����̨
			while (count < taskNum){
				Random random = new Random();
				int x = random.nextInt(rowNum);
				int y = random.nextInt(colNum);
				if (isUsefulPoint(Points[x][y]) && isStartPoint(Points[x][y]) && Points[x][y].isOccupiedByPod()){
					int z = random.nextInt(WorkStations.size());
					Point workStation = WorkStations.get(z);
					tempString +=  "\r\n" + x + " " + y + " " + workStation.getX() + " " + workStation.getY();
					count++;
				}
			}
		}else{
			while (count < taskNum){
				Random random = new Random();
				int x1 = random.nextInt(rowNum);
				int y1 = random.nextInt(colNum);
				int x2 = random.nextInt(rowNum);
				int y2 = random.nextInt(colNum);
				if (isUsefulPoint(Points[x1][y1])&&isUsefulPoint(Points[x2][y2])){
					tempString +=  "\r\n" + x1 + " " + y1 + " " + x2 + " " + y2;
					count++;
				}
			}
		}
		
		WriteData.writeData(taskLocation, tempString);
	}
	public void generateAgv(int agvNum, String agvLocation){
		String tempString = String.valueOf(agvNum)+"\r\n";
		int count = 0;
		boolean [][] hasAgv = new boolean [rowNum][colNum];
		while (count < agvNum){
			Random random = new Random();
			int x1 = random.nextInt(rowNum);
			int y1 = random.nextInt(colNum);
			if (isUsefulPoint(Points[x1][y1])&&!hasAgv[x1][y1]){
				tempString +=  "\r\n" +  + (1000+count+1) + " " + x1 + " " + y1 ;
				hasAgv[x1][y1] = true;
				count++;
			}
		}
		WriteData.writeData(agvLocation, tempString);
	}
}
