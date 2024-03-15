package MultiCooperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

import Application.WriteData;
import Model.*;
import TaskAssignment.TaskDivide;

public class MultiCooperation {

	/**
	 * 存储所有地图信息
	 */
	private MapInfo mapInfo = new MapInfo();

	private TaskDivide taskDivide = new TaskDivide();

	/**
	 * 储存所有AGV编号
	 */
	private ArrayList<String> AGVCodes= new ArrayList<>();
	/**
	 * 储存所有Pod编号
	 */
	private ArrayList<String> PodCodes= new ArrayList<>();
	/**
	 * 存储所有AGV信息
	 */
	private HashMap <String, Agv> AGVs = new HashMap<>();
	/**
	 * 存储所有Pod信息
	 */
	private HashMap <String, Pod> Pods = new HashMap<>();
	/**
	 * 是否使用动态路径规划给路网添加权重
	 */
	private boolean isDynamicPathPlanning = false;
	/**
	 * 存储所有任务统计信息
	 */
	private ArrayList<String> taskStatisticData = new ArrayList<>();
	/**
	 * 存储所有AGV统计信息
	 */
	private ArrayList<String> agvStatisticData = new ArrayList<>();
	/**
	 * 运行周期数
	 */
	private int totalTime = 0;
	private int totalCarryTimes = 0;
	private int totalWaitTime = 0;
	private int totalDeadLockTimes = 0;
	private int totalBlockingTimes = 0;
	/**
	 * 当前运行的多车协同策略
	 */
	private String multiCooperationStrategy = MultiCooperationStrategy.DeadLockDetectionAndRecovery;

	public MultiCooperation(){
		// 数据初始化
		taskStatisticData.add(WriteData.taskStatisticStructure);
		agvStatisticData.add(WriteData.agvStatisticStructure);
		totalTime = 0;
		totalCarryTimes = 0;
		totalWaitTime = 0;
		totalDeadLockTimes = 0;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public void addTotalTime(){
		this.totalTime++;
	}
	public void removeTotalTime(){
		this.totalTime--;
	}
	public String getMultiCooperationStrategy() {
		return multiCooperationStrategy;
	}
	public void setMultiCooperationStrategy(String multiCooperationStrategy) {
		this.multiCooperationStrategy = multiCooperationStrategy;
		if(MultiCooperationStrategy.FuturePathPlanningWithRecovery.equals(multiCooperationStrategy)||
		   MultiCooperationStrategy.FuturePathPlanningWithAvoidance.equals(multiCooperationStrategy)){
			isDynamicPathPlanning = true;
		}else{
			isDynamicPathPlanning = false;
		}
	}
	public MapInfo getMapInfo() {
		return mapInfo;
	}
	public void setMapInfo(MapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}
	public ArrayList<String> getAGVCodes() {
		return AGVCodes;
	}
	public ArrayList<String> getPodCodes() {
		return PodCodes;
	}
	public void setAGVCodes(ArrayList<String> aGVCodes) {
		AGVCodes = aGVCodes;
	}
	public void setPodCodes(ArrayList<String> podCodes) {
		PodCodes = podCodes;
	}
	public HashMap<String, Agv> getAGVs() {
		return AGVs;
	}
	public HashMap<String, Pod> getPods() {
		return Pods;
	}
	public void setAGVs(HashMap<String, Agv> aGVs) {
		AGVs = aGVs;
	}
	public void setPods(HashMap<String, Pod> pods) {
		Pods = pods;
	}

	/**
	 * 根据XY得到mapInfo中的点
	 * @param X
	 * @param Y
	 * @return
	 */
	public Point getPoint(int X, int Y){
		if (X>=mapInfo.getRowNum()||Y>=mapInfo.getColNum()){
			return null;
		}
		return mapInfo.getPoints()[X][Y];
	}

	/**
	 * 根据XY得到mapInfo中距离该点最近的空闲AGV
	 * @param X
	 * @param Y
	 * @return
	 */
	public Agv getNearestAgv(int X, int Y){
		Point startPoint = getPoint(X,Y);
		double distance = Double.MAX_VALUE;
		Agv agvResult = null;
		for (String agvCode:AGVs.keySet()){
			Agv agv = AGVs.get(agvCode);
			if (AgvState.Free.equals(agv.getState())){
				Point currentPoint = getPoint(agv.getX(), agv.getY());
				double distance1 = mapInfo.Astar(currentPoint, startPoint, false).getDistance();
				if (distance1 < distance){
					distance = distance1;
					agvResult = agv;
				}
			}
		}
		return agvResult;
	}

	/**
	 * 根据XY得到mapInfo中距离该点最近的未被分配和占用的空货位
	 * @param X
	 * @param Y
	 * @return
	 */
	public Point getNearestPoint (int X, int Y){
		Point startPoint = getPoint(X,Y);
		Point result = null;
		double distance = Double.MAX_VALUE;
		for (int i = mapInfo.getPodArea().size() - 1; i >= 0; i-- ){
			if (!mapInfo.getPodArea().get(i).isOccupiedByPod() && !mapInfo.getPodArea().get(i).isAssignedForPod()){
				double distance1 = Math.abs(mapInfo.getPodArea().get(i).getX() - X) + Math.abs(mapInfo.getPodArea().get(i).getY() - Y);
				if (distance1 < distance){
					distance1 = mapInfo.Astar(mapInfo.getPodArea().get(i), startPoint, false).getDistance();
					if (distance1 < distance){
						distance = distance1;
						result = mapInfo.getPodArea().get(i);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 根据货架编号得到正扛着改货物或者被分配了该货物的Agv
	 * @param podCode
	 * @return
	 */
	public Agv findByWayAgv (String podCode) {
		for (String agvCode : AGVCodes) {
			Agv agv = AGVs.get(agvCode);
			if (agv.isLoading() && agv.getPodCode().equals(podCode)){
				return agv;
			}
		}
		for (String agvCode : AGVCodes) {
			Agv agv = AGVs.get(agvCode);
			if (agv.getState().equals(AgvState.Working)){
				for (int i = 0; i < agv.getStaticDestination().size(); i++){
					if (agv.getStaticDestination().get(i).isOccupiedByPod()
							&& agv.getStaticDestination().get(i).getOccupiedPodCode().equals(podCode)){
						return agv;
					}
				}
			}

		}

		return null;
	}

	/**
	 * 测试此时系统中的各项参数是否正常
	 * @param agv
	 * @return
	 */
	public void test (Agv agv){
		//每时每刻货架中的货架和被AGV扛着的货架之和是300
		int count = 0;
		for (int i = 0; i < mapInfo.getRowNum(); i++){
			for (int j = 0; j < mapInfo.getColNum(); j++){
				if (getPoint(i,j).isOccupiedByPod() || (i == agv.getX() && j == agv.getY() && agv.isLoading())){
					count ++;
				}
			}
		}
		if (count != mapInfo.getPods().size()){
			System.out.println("pod Number is wrong");
		}
		//判断每个block空货位的区域是不是和记录的相匹配
		int totalPod = 0;
		for (int i = 0; i < mapInfo.getBlockNum(); i++){
			Block b = mapInfo.getBlocks().get(i);
			int tmp = 0;
			for (int j = 0; j < b.getRowNum(); j++){
				for (int k = 0; k < b.getColNum(); k++){
					if (!getPoint(j + b.getBlockStart().getX(),k +b.getBlockStart().getY()).isOccupiedByPod()){
						tmp++;
					}
				}
			}
			if (tmp != b.getEmptyPodLocation().size()){
				System.out.println("emptyPodLocation is wrong");
			}
			totalPod = totalPod + b.getPodCode().size();
		}
		if (totalPod > mapInfo.getPods().size()){
			System.out.println("too much pod");
		}
		for (int i = 0;  i < getMapInfo().getBlockNum(); i ++){
			Block b = getMapInfo().getBlocks().get(i);
			if (b.getContainPod().size() != b.getPodCode().size()){
				for (Integer tmp: b.getPodCode()){
					if (!b.getContainPod().containsKey(tmp)){
						System.out.println(tmp);
					}
				}
				System.out.println("podNum is wrong");
			}
			if (b.getContainPod().size() + b.getEmptyPodLocation().size() != b.getRowNum() * b.getColNum()){
				System.out.println("emptyLocation is wrong");
			}
		}


		//AGV最后一个任务不是货架区，或者扛着空货架去拣选站，或者扛着货架去有货架的地方
		if (agv.getStaticDestination().size() > 0){
			if (!agv.getStaticDestination().get(agv.getStaticDestination().size() - 1).getType().equals(PointType.Pallet)){
				System.out.println("problem1 appears");
			}
			if (agv.getStaticDestination().get(0).getType().equals(PointType.WorkStation) && !agv.isLoading()){
				System.out.println("problem2 is here");
			}
			for (int i = 0; i < agv.getStaticDestination().size(); i++){
				if (agv.getStaticDestination().get(i).getType().equals(PointType.Undefined)){
					/*System.out.println("problem3 is here");
					System.out.println(agv.getStaticDestination().get(i).getX() + " " + agv.getStaticDestination().get(i).getY());*/
				}
			}
			if (agv.getRoute() != null && agv.getDestination() != null){
				if (agv.getDestination().size() > 0 && agv.getRoute().size() > 0){
					if (mapInfo.isSamePoint(agv.getRoute().get(agv.getRoute().size() - 1), agv.getDestination().get(0))){
					//	System.out.println("problem is here");
					}
				}
			}
			if (!agv.isLoading() && !agv.getStaticDestination().get(0).isOccupiedByPod()
					&& agv.getStaticDestination().get(0).getType().equals(PointType.Pallet)){
				System.out.println("problem4 is here");
			}

			if (agv.getDestination().size() > agv.getStaticDestination().size() && agv.getRoute() != null && agv.getRoute().size() > 2){
				System.out.println("problem5 is here");
			}

		}
		//Agv在一个地方，但是这个点显示没有Agv
		if(!getPoint(agv.getX(),agv.getY()).isOccupied()){
			System.out.println("problem is here");
		}

		//AGV要去一个没有货架的地方扛货架
		/*if (agv.getStaticDestination().size() == 2 && !agv.isLoading()){
			if (agv.getStaticDestination().get(agv.getStaticDestination().size() - 1).isOccupiedByPod()){
				Pod pod = getPods().get(agv.getStaticDestination().get(agv.getStaticDestination().size() - 1).getOccupiedPodCode());
				if (!pod.isLoading() && !pod.isAssigned()){
				    System.out.println("problem is here");
                }
			}
		}*/
		//AGV扛着货架但是没有货架编号
		if (!agv.isLoading() && agv.getPodCode() != null){
			System.out.println("problem is here");
		}

	}


	public void findNewPodArea (Agv agv,Point oldPoint){
		agv.getStaticDestination().get(0).setAssignedForPod(false);
		Point newLocation = mapInfo.findStayLocationByPoint(oldPoint);
		if (newLocation == null){
			Point stop = getPoint(agv.getX(),agv.getY());
			ArrayList<Point> newRoute = new ArrayList<>();
			if (agv.getRoute() == null){
				for (int i = 0; i < 5; i ++) {
					newRoute.add(stop);
				}
				agv.setRoute(newRoute);
			}
			else{
				for (int i = 0; i < 5; i ++) {
					agv.getRoute().add(0, stop);
				}
			}
		}
		else{
			newLocation.setAssignedForPod(true);
			agv.setRoute(null);
			agv.getStaticDestination().set(0,newLocation);
			agv.getDestination().clear();
			for (int j = 0; j < agv.getStaticDestination().size(); j++){
				agv.getDestination().add(agv.getStaticDestination().get(j));
			}
		}

	}

	public Pod findPodByLoc(int x, int y){
		int distance = Integer.MAX_VALUE;
		Pod ret = null;
		for (String podCode : PodCodes){
			Pod pod = Pods.get(podCode);
			if (pod.isAssigned() == false){
				if (distance > (Math.abs(pod.getX() - x) + Math.abs(pod.getY() - y))){
					ret = pod;
					distance = Math.abs(pod.getX() - x) + Math.abs(pod.getY() - y);
				}
			}
		}
		return ret;
	}

	public void executeTask(){


		boolean changed = false;

		//Collections.shuffle(AGVCodes);//打乱AGV顺序会降低系统运行效率



		for (String agvCode:AGVCodes){

			Agv agv = AGVs.get(agvCode);

			test(agv);



			if (Integer.valueOf(agvCode) == 1003){
				boolean flag =false;
			}


			if(agv.isLoading() && agv.getStaticDestination().get(0).isOccupiedByPod()
					&& !getPods().get(agv.getStaticDestination().get(0).getOccupiedPodCode()).isLoading()
					&& agv.getStaticDestination().get(0).getType().equals(PointType.Pallet)){
				//存在车走着走着没路了就将该货架临时停靠被分配的货架上，需要将该点置为为分配，并为原本分配该点的AGV重新分配货架点
				findNewPodArea(agv,agv.getStaticDestination().get(0));
			}

			// 规划路径
			if (agv.getRoute() == null || agv.getRoute().size()<=1){
				if (agv.getDestination().size() > 0){
					designPath(agv);
				}
			}
			// AGV前进
			if (AgvState.Working.equals(agv.getState())){

				// 只要有小车处于工作状态，系统时间+1
				if (!changed){
					addTotalTime();
					changed = true;
				}
				// 小车工作时间+1
				agv.addRunTime();
				if (checkIsRun(agv)){
					moveAgv(agv);
				}
				else if (isMovedIn(agv)){
					// 情况1：扛着货架的AGV要将货架放回没有路径的货架区
					// 此时需要给当前的AGV增加新的特殊的任务
					moveOtherPodIn(agv);
					if (getAGVCodes().size() > 1) {
						totalBlockingTimes++;
					}
				}
				else if (isBlockedByNewPod(agv)){
					agv.getRoute().clear();
					Point currentPoint = getPoint(agv.getX(), agv.getY());
					RouteResult routeResult = mapInfo.Astar(currentPoint, agv.getStaticDestination().get(0), agv.isLoading());
					if (routeResult.getRoute().size() != 0){
						agv.setRoute(routeResult.getRoute());
					}
					else{
						agv.getDestination().add(0,agv.getStaticDestination().get(0));
					}
					if (getAGVCodes().size() > 1) {
						totalBlockingTimes++;
					}
				}
				else if (isBlockBySelf(agv)){
					//扛着货架的AGV被走道上原本自己要扛的货架给block住了
					deadBlockedByself(agv);
				}
				else{
					if (MultiCooperationStrategy.DeadLockDetectionAndRecovery.equals(multiCooperationStrategy)){
						deadLockDetectionAndRecovery(agv);
					}
					if (agv.getTasks() != null && agv.getTasks().size() > 0){
						agv.getTasks().get(0).addWaitTime();
					}
					totalWaitTime ++;
				}

			}
		}
	}


	public boolean isBlockBySelf(Agv agv) {
		Point nextPoint = agv.getRoute().get(1);
		if (agv.isLoading() && nextPoint.isOccupiedByPod() && !getPods().get(nextPoint.getOccupiedPodCode()).isLoading()
				&& getPods().get(nextPoint.getOccupiedPodCode()).isAssigned()){
			for (int i = 0; i < agv.getStaticDestination().size(); i++){
				if (mapInfo.isSamePoint(agv.getStaticDestination().get(i),nextPoint)){
					return true;
				}
			}
		}
		return false;
	}


	//TODO:重新梳理这块的逻辑，考虑到被挡住的货架可能全都在货架区之内
	//密集存储时，不一定要一次处理所有的，可以一个一个解决
	public void deadBlockedByself (Agv agv){
		Point nextPoint = agv.getRoute().get(1);
		Point problemPoint = new Point();
		Point problemDestination = new Point();
		int position = 0;
		if (agv.isLoading() && nextPoint.isOccupiedByPod() && !getPods().get(nextPoint.getOccupiedPodCode()).isLoading()
				&& getPods().get(nextPoint.getOccupiedPodCode()).isAssigned()){
			for (int i = 0; i < agv.getStaticDestination().size() - 1; i++){
				if (mapInfo.isSamePoint(agv.getStaticDestination().get(i),nextPoint)){
					problemPoint = nextPoint;
					problemDestination = agv.getStaticDestination().get(i + 1);
					position = i;
					break;
				}
			}
		}
		Point newDestination = getNearestPoint(problemPoint.getX(), problemPoint.getY());
		if (newDestination == null){
			if (agv.getRoute() == null){
				ArrayList<Point> newRoute = new ArrayList<>();
				for (int i = 0; i < 5; i++){
					newRoute.add(getPoint(agv.getX(),agv.getY()));
				}
				agv.setRoute(newRoute);
			}
			else{
				for (int i = 0; i < 5; i++){
					agv.getRoute().add(0,getPoint(agv.getX(),agv.getY()));
				}
			}
		}
		else{
			if (agv.getStaticDestination().get(position + 1).getType().equals(PointType.WorkStation)){
				agv.getStaticDestination().get(position + 2).setAssignedForPod(false);
				agv.getStaticDestination().remove(position);
				agv.getStaticDestination().remove(position);
				agv.getStaticDestination().remove(position);
				agv.getStaticDestination().add(0,getPoint(agv.getX(),agv.getY()));
				agv.getStaticDestination().add(0,newDestination);
				agv.getStaticDestination().add(0,problemDestination);
				agv.getStaticDestination().add(0,problemPoint);


			}
			else{
				agv.getStaticDestination().get(position + 1).setAssignedForPod(false);
				agv.getStaticDestination().remove(position);
				agv.getStaticDestination().remove(position);
				agv.getStaticDestination().add(0,getPoint(agv.getX(),agv.getY()));
				agv.getStaticDestination().add(0,newDestination);
				agv.getStaticDestination().add(0,problemPoint);
			}
			newDestination.setAssignedForPod(true);
			Pod pod = getPods().get(agv.getPodCode());
			pod.setLoading(false);
			pod.setAgvCode(null);

			agv.getDestination().clear();
			for (int i = 0; i < agv.getStaticDestination().size(); i++){
				agv.getDestination().add(agv.getStaticDestination().get(i));
			}


			agv.setLoading(false);
			agv.setPodCode(null);
			agv.setRoute(null);
		}

	}


	//情况一：扛着货架的AGV要将货架放回没有路径的货架区
	public boolean isMovedIn (Agv agv){
		Point nextPoint = agv.getRoute().get(1);
		if (agv.isLoading() && agv.getStaticDestination().get(0).getType().equals(PointType.WorkStation)){
			return false;
		}
		// 1.下一个点一定是货架区且有货架且没有AGV
		if (nextPoint.getType().equals(PointType.Pallet) && nextPoint.isOccupiedByPod() && !nextPoint.isOccupied()){
			Pod nextPod = getPods().get(nextPoint.getOccupiedPodCode());
			// 2.下一个货架没有被分配给别的AGV
			if (!nextPod.isAssigned()){
				return true;
			}
		}
		return false;
	}


	public boolean isBlockedByNewPod(Agv agv){
		Point nextPoint = agv.getRoute().get(1);
		if (agv.isLoading()){
			// 1.下一个点一定是货架区且有货架且没有AGV
			if (nextPoint.getType().equals(PointType.Pallet) && nextPoint.isOccupiedByPod() && !nextPoint.isOccupied()){
				Pod nextPod = getPods().get(nextPoint.getOccupiedPodCode());
				// 2.下一个货架没有被分配给别的AGV
				if (!nextPod.isAssigned()){
					return true;
				}
			}
		}
		return false;
	}

	//需要把挡着的AGV移到货架内部去
	//正对这些需要搬动的货架，isAssigned = true
	//最终目的还是将这个AGV之前扛得货架移动进去
	public void moveOtherPodIn (Agv agv){
		ArrayList<Point> newTasks = new ArrayList<>();
		Point lastEmpty = new Point();
		boolean firstMove = true;
		ArrayList<Point> path = new ArrayList<>();
		for (int i = 0; i < agv.getRoute().size(); i++){
		    if ((i == 0) || !mapInfo.isSamePoint(agv.getRoute().get(i),agv.getRoute().get(i - 1))){
		        path.add(agv.getRoute().get(i));
            }
        }
		for (int i = path.size() - 2; i >= 1; i--){
			Point p = path.get(i);
			if (p.isOccupiedByPod()){
				// 这个货架未被执行中 且 这个货架没有被分配即将执行,确保第一个货架被搬回原目的地
				Pod pod = getPods().get(p.getOccupiedPodCode());

				if (!pod.isAssigned() && !pod.isLoading()){
					newTasks.add(p);
					if (firstMove){
						newTasks.add(path.get(path.size() - 1));
						lastEmpty = getPoint(p.getX(),p.getY());
						firstMove = false;
					}
					else{
						// 1.注意agv.getRoute()可能已经有isAssignedForPod为true的点
                        // 2.注意新分配完的地址就要将其置为分配给别的货架
						newTasks.add(lastEmpty);
						lastEmpty.setAssignedForPod(true);
						lastEmpty = getPoint(p.getX(),p.getY());
					}
				}
			}
		}
		newTasks.add(getPoint(agv.getX(),agv.getY()));
		newTasks.add(lastEmpty);
		lastEmpty.setAssignedForPod(true);

		Pod pod = getPods().get(agv.getPodCode());
		pod.setLoading(false);
		pod.setAgvCode(null);

		agv.setLoading(false);
		agv.setPodCode(null);
		agv.setRoute(null);

		agv.getTasks().get(0).addCarryTimes();
		totalCarryTimes ++;

		ArrayList<Point> tmp = new ArrayList<>();
		for (int i = 0; i < agv.getDestination().size(); i++){
			tmp.add(agv.getDestination().get(i));
		}
		agv.getDestination().clear();
		agv.getStaticDestination().clear();
		for (Point p: newTasks){
			agv.getStaticDestination().add(p);
			agv.getDestination().add(p);

			if (p.isOccupiedByPod()){
				Pod pod1 = getPods().get(p.getOccupiedPodCode());
				pod1.setAssigned(true);
				pod1.setAssignedAgvCode(agv.getAgvCode());
			}
		}
		for (Point p: tmp){
			agv.getStaticDestination().add(p);
			agv.getDestination().add(p);
		}
	}

	public void designPath(Agv agv){
		Point currentPoint = getPoint(agv.getX(), agv.getY());
		RouteResult routeResult = mapInfo.Astar(currentPoint, agv.getDestination().get(0), agv.isLoading());

		if (routeResult.getRoute().size() < 1){
			//如果是出发点出现了新的货架挡住了出去，那么添加新的任务进行规划
			if(isNewPodAppear(agv,currentPoint)){
				boolean isAddTask = addNewTask(agv,currentPoint);
				routeResult = mapInfo.Astar(currentPoint, agv.getDestination().get(0), agv.isLoading());
				if (isAddTask == false){
					int direction = mapInfo.calcDirction(currentPoint);
					if (mapInfo.Astar(currentPoint, agv.getDestination().get(0), false).getRoute().size() < 2){
						System.out.println("here");
					}
					int[] dirX = {-1,1,0,0};
					int[] dirY = {0,0,-1,1};
					Point destination = getPoint(currentPoint.getX() + dirX[direction] , currentPoint.getY() + dirY[direction]);
					if (!mapInfo.isSamePoint(agv.getDestination().get(0),destination)){
						agv.getDestination().add(0, destination);
					}
					routeResult = mapInfo.Astar(currentPoint, destination, false);
				}
			}
			else if (isBlockIn(agv)){
				//如果目的地出现新的货架挡住了进入，那么需要先将货架运到新目标地点的走道边，然后再搬运进去
				int direction = mapInfo.calcDirction(agv.getDestination().get(0));
				routeResult = dealBlockIn(agv,direction);
			}
		}
		if (routeResult.getRoute().size() >= 1){

			agv.setRoute(routeResult.getRoute());
			
			agv.setState(AgvState.Working);
			agv.getDestination().remove(0);
			// AGV的起点和终点在同一个点，添加一个点，因为需要消耗一个周期处理该步骤
			if (agv.getRoute().size()==1){
				agv.getRoute().add(currentPoint);
			}
			if (agv.getTasks()!=null){
				Task task = agv.getTasks().get(0);
			}

		}else{
			agv.setRoute(null);
			agv.setState(AgvState.Free);
			System.out.println(String.format("Can not Find a path from [%s,%s] to [%s,%s]", currentPoint.getX(),currentPoint.getY(),agv.getDestination().get(0).getX(),agv.getDestination().get(0).getY()));
		}
	}

	public boolean isNewPodAppear (Agv agv, Point currentPoint){
		//规划路径的时候，出现新的货架挡住了要出去的货架
		boolean blockOut = mapInfo.isBlocked(currentPoint,getMapInfo().getWorkStations().get(0));
		if (agv.isLoading() && currentPoint.getType().equals(PointType.Pallet) && blockOut) {
			return true;
		}
		return false;
	}

	public boolean addNewTask (Agv agv, Point currentPoint){
		ArrayList<Point> newTask = getNewTask(agv,currentPoint);
		if (newTask.size() == agv.getDestination().size() + 1){
			return false;
		}
		//区分死锁和被新出现的货架block住的区别
		if (agv.getDestination() != null && agv.getDestination().size() > 0){
			Point destination = agv.getDestination().get(0);
			if (Math.abs(destination.getX() - agv.getX()) + Math.abs(destination.getY() - agv.getY()) <= 1){
				if (destination.isOccupied() && !destination.isOccupiedByPod()){
					return false;
				}
				if (destination.isOccupiedByPod() && getPods().get(destination.getOccupiedPodCode()).isAssigned()){
					return false;
				}

				if (destination.isOccupiedByPod() && destination.isOccupied()){
					if (getAGVs().get(destination.getOccupiedAgvCode()).isLoading() || getPods().get(destination.getOccupiedPodCode()).isAssigned()){
						return false;
					}
				}
			}
		}

        Pod pod = getPods().get(agv.getPodCode());
        pod.setLoading(false);

		agv.getDestination().clear();
		agv.getStaticDestination().clear();
		agv.setLoading(false);
        agv.setPodCode(null);

		agv.getTasks().get(0).addCarryTimes();
		totalCarryTimes ++;

		for (int i = 0; i < newTask.size() - 1; i++){
			Point p = newTask.get(i);
			if (p.isOccupiedByPod()){
				Pod pod1 = getPods().get(p.getOccupiedPodCode());
				pod1.setAssigned(true);
				pod1.setAssignedAgvCode(agv.getAgvCode());
				if (Integer.valueOf(pod1.getPodCode()) == 44 && Integer.valueOf(agv.getAgvCode()) == 1009){
					System.out.println("here");
				}
			}
			agv.getStaticDestination().add(p);
			agv.getDestination().add(p);
		}
		agv.getStaticDestination().add(newTask.get(newTask.size() - 1));
		agv.getDestination().add(newTask.get(newTask.size() - 1));

		return true;
	}

	public ArrayList<Point> getNewTask (Agv agv, Point point){
		ArrayList<Point> newTask = new ArrayList<>();
		Point destination = agv.getDestination().get(0);
		if (mapInfo.isBlocked(point,destination)){
			int direction = mapInfo.calcDirction(point);
			ArrayList<Pod> movePod = mapInfo.needMove(point,direction);
			for (Pod p : movePod){
				//要将新获得的货架点设计为已分配
				Point newLocation = mapInfo.findStayLocation(getPoint(agv.getX(),agv.getY()),p,direction);
				if (newLocation == null){
					for (int i = 0; i < mapInfo.getRowNum(); i++){
						for (int j = 0; j < mapInfo.getColNum(); j++){
							if (mapInfo.getPoints()[i][j].isAssignedForPod()){
								mapInfo.getPoints()[i][j].setAssignedForPod(false);
							}
						}
					}

					for (String agvCode:AGVCodes) {

						Agv TmpAgv = AGVs.get(agvCode);

						if (TmpAgv.getStaticDestination() != null && TmpAgv.getStaticDestination().size() > 0){
							for (int i = 0; i < TmpAgv.getStaticDestination().size(); i++){
								if (TmpAgv.getStaticDestination().get(i).getType().equals(PointType.Pallet) && !TmpAgv.getStaticDestination().get(i).isOccupiedByPod()){
									TmpAgv.getStaticDestination().get(i).setAssignedForPod(true);
								}
							}
						}

					}
					newLocation = mapInfo.findStayLocation(getPoint(agv.getX(),agv.getY()),p,direction);
				}

				newLocation.setAssignedForPod(true);
				newTask.add(getPoint(p.getX(),p.getY()));
				newTask.add(newLocation);
			}
		}
		newTask.add(point);
		for (int i = 0; i < agv.getStaticDestination().size(); i++){
			newTask.add(agv.getStaticDestination().get(i));
		}
		return newTask;
	}

	public boolean isBlockIn(Agv agv) {
		//规划路径的时候，出现新的货架挡住了新的目的位置

		boolean blockIn = mapInfo.isBlocked(getMapInfo().getWorkStations().get(0),agv.getDestination().get(0));
		if (agv.isLoading() && blockIn) {
			return true;
		}
		return false;
	}

	public RouteResult dealBlockIn (Agv agv, int direction){
		Point currentPoint = getPoint(agv.getX(),agv.getY());
		Block b = mapInfo.getBlocks().get(agv.getDestination().get(0).getBlockId());
		Point destination = agv.getDestination().get(0);
		if (direction == 0){
			if (destination.getY() == b.getBlockStart().getY() && b.getBlockStart().getY() != 0){
				direction = 2;
			}
			else if (destination.getY() == b.getBlockStart().getY() + b.getColNum() - 1 && b.getBlockStart().getY() + b.getColNum() != mapInfo.getColNum()){
				direction = 3;
			}
		}

		Point newPoint = new Point();
		if (direction == 0){
			newPoint = getPoint(b.getBlockStart().getX() - 1,agv.getDestination().get(0).getY());
		}
		else if (direction == 1){
			newPoint = getPoint(b.getBlockStart().getX() + b.getRowNum(),agv.getDestination().get(0).getY());
		}
		else if (direction == 2){
			newPoint = getPoint(agv.getDestination().get(0).getX(),b.getBlockStart().getY() - 1);
		}
		else if (direction == 3){
			newPoint = getPoint(agv.getDestination().get(0).getX(),b.getBlockStart().getY() + b.getColNum());
		}

		RouteResult routeResult = mapInfo.Astar(currentPoint, newPoint, true);
		RouteResult routeResult2 = mapInfo.Astar(newPoint, agv.getDestination().get(0), false);
		for (int i = 1; i < routeResult2.getRoute().size(); i++){
			routeResult.getRoute().add(routeResult2.getRoute().get(i));
			routeResult.setDistance(routeResult.getRoute().size());
		}
		return routeResult;
	}

	public boolean checkIsRun(Agv agv){
		Point nextPoint = agv.getRoute().get(1);

		if (nextPoint.getX() == agv.getX() && nextPoint.getY() == agv.getY()){
		    return true;
        }
		
		if ((nextPoint.getOccupiedAgvCode() == null || nextPoint.getOccupiedAgvCode().equals(agv.getAgvCode())) && !agv.isLoading()){
			agv.setStopByAgvCode(null);
			agv.setStopByPodCode(null);
			return true;
		}
		else if (!nextPoint.isOccupied() && !nextPoint.isOccupiedByPod() && agv.isLoading()){
			agv.setStopByAgvCode(null);
			agv.setStopByPodCode(null);
			return true;
		}
		return false;
	}

	public void deadLockDetectionAndRecovery(Agv agv){
		Point nextPoint = agv.getRoute().get(1);
		boolean isDeadLock = false;
		boolean hasChanged = false;
		// 发现AGV被别的AGV阻挡或货架阻挡，判断是否存在死锁
            if ((agv.isLoading() && (nextPoint.isOccupiedByPod()) ||nextPoint.isOccupied())){
                if (nextPoint.isOccupied() && !nextPoint.getOccupiedAgvCode().equals(agv.getAgvCode())){
                    agv.setStopByAgvCode(nextPoint.getOccupiedAgvCode());
                }
                else{
                    agv.setStopByAgvCode(null);
                }
                if (nextPoint.isOccupiedByPod() && agv.isLoading()){
                    agv.setStopByPodCode(nextPoint.getOccupiedPodCode());
                }
                if (agv.isLoading() && agv.getStopByAgvCode() == null && agv.getStopByPodCode() != null){
                    if (getPods().get(agv.getStopByPodCode()).isAssigned() && !getPods().get(agv.getStopByPodCode()).isLoading()){
                        if (findAgvByPod(agv,getPods().get(agv.getStopByPodCode())) == null){
                            boolean flag = false;
                        }
                        agv.setStopByAgvCode(findAgvByPod(agv,getPods().get(agv.getStopByPodCode())).getAgvCode());
                    }
                    else if (nextPoint.isOccupiedByPod() && !nextPoint.isOccupied() && nextPoint.getType().equals(PointType.CrossRoad)){
                        agv.setStopByAgvCode(findAgvByPoint(nextPoint).getAgvCode());
                    }
                }

                isDeadLock = checkDeadLock(agv);
                if (isDeadLock){
                    hasChanged = changeRoute(agv);
                    if (hasChanged){
                        if (agv.getTasks() != null &&agv.getTasks().size() > 0){
                            agv.getTasks().get(0).addDeadlockTimes();
                        }
                        totalDeadLockTimes ++;
                    }
                }
            }
	}

	public Agv findAgvByPoint(Point nextPoint){
		for (String agvCode:AGVCodes) {
			Agv agv = AGVs.get(agvCode);
			for (int i = 0; i < agv.getStaticDestination().size(); i++){
				if (agv.getStaticDestination().get(i).getX() == nextPoint.getX()
						&& agv.getStaticDestination().get(i).getY() == nextPoint.getY()){
					return agv;
				}
			}


		}
		return null;
	}

	
	public boolean checkDeadLock(Agv agv){
		Agv nextAgv = AGVs.get(agv.getStopByAgvCode());

		boolean isDeadLock = false;
		ArrayList<String> currentList = new ArrayList<>();
		// 如果起点是蝌蚪环的尾巴，就会陷入死循环。
		while (nextAgv != null){
			if (currentList.contains(nextAgv.getAgvCode())){
				break;
			}else{
				currentList.add(nextAgv.getAgvCode());
			}
			if (nextAgv.getAgvCode().equals(agv.getAgvCode())||nextAgv.getState().equals(AgvState.Free)){
				isDeadLock = true;
				break;
			}else {
				nextAgv = AGVs.get(nextAgv.getStopByAgvCode());
			}
		}
		agv.setAlterable(true);

		return isDeadLock;
	}

	public Agv findAgvByPod (Agv agv, Pod pod){
		for (String agvCode:AGVCodes) {
			Agv agvTmp = AGVs.get(agvCode);
			if (agvTmp.getAgvCode().equals(agv.getAgvCode())){
				continue;
			}
			if (agvTmp.getState().equals(AgvState.Working)){
				for (int i = 0; i < agvTmp.getStaticDestination().size(); i++){
					if (agvTmp.getStaticDestination().get(i).isOccupiedByPod()
							&& getPods().get(agvTmp.getStaticDestination().get(i).getOccupiedPodCode()).getPodCode().equals(pod.getPodCode())){
						return agvTmp;
					}
				}
			}
		}
		return null;
	}

	
	public boolean changeRoute(Agv agv){
		boolean hasChanged = false;
		Agv nextAgv = AGVs.get(agv.getStopByAgvCode());

		if (nextAgv == null){
			nextAgv = agv;
		}

		//map2 agv4
		if (!agv.isLoading() && nextAgv.isLoading()){
			ArrayList<Point> roundPoint = new ArrayList<>();
			boolean changeFlag = true;
			if (agv.getY() == nextAgv.getY()){
				if (agv.getY() != 0){
					roundPoint.add(getPoint(agv.getX(),agv.getY() - 1));
					roundPoint.add(getPoint(nextAgv.getX(),nextAgv.getY() - 1));
				}
				if (agv.getY() < mapInfo.getColNum() - 1){
					roundPoint.add(getPoint(agv.getX(),agv.getY() + 1));
					roundPoint.add(getPoint(nextAgv.getX(),nextAgv.getY() + 1));
				}
				if (Math.min(agv.getX(),nextAgv.getX()) > 0){
					roundPoint.add(getPoint(Math.min(agv.getX(),nextAgv.getX()) - 1,agv.getY()));
				}
				if (Math.max(agv.getX(),nextAgv.getX()) < mapInfo.getRowNum() - 1){
					roundPoint.add(getPoint(Math.max(agv.getX(),nextAgv.getX()) + 1,agv.getY()));
				}
			}
			else if (agv.getX() == nextAgv.getX()){
				if (agv.getX() != 0){
					roundPoint.add(getPoint(agv.getX() - 1,agv.getY()));
					roundPoint.add(getPoint(nextAgv.getX() - 1,nextAgv.getY()));
				}
				if (agv.getX() < mapInfo.getRowNum() - 1){
					roundPoint.add(getPoint(agv.getX() + 1,agv.getY()));
					roundPoint.add(getPoint(nextAgv.getX() + 1,nextAgv.getY()));
				}
				if (Math.min(agv.getY(),nextAgv.getY()) > 0){
					roundPoint.add(getPoint(agv.getX(),Math.min(agv.getY(),nextAgv.getY()) - 1));
				}
				if (Math.max(agv.getY(),nextAgv.getY()) < mapInfo.getColNum() - 1){
					roundPoint.add(getPoint(agv.getX(),Math.max(agv.getY(),nextAgv.getY()) + 1));
				}
			}
			for (int i = 0 ; i < roundPoint.size(); i++){
				if (roundPoint.get(i).isOccupied()){
					changeFlag = false;
				}
			}
			if (changeFlag){
				Agv tmp = agv;
				agv = nextAgv;
				nextAgv = tmp;
			}

		}
		if (nextAgv.isAlterable()){
			Point outPoint = null;
			Point currentPoint = getPoint(nextAgv.getX(), nextAgv.getY());
			// 找出口点，及重新添加目的地
			if (!nextAgv.getState().equals(AgvState.Free) && nextAgv.getStaticDestination().size()>0 && nextAgv.getRoute() != null){
				outPoint = mapInfo.getOutPoint(nextAgv.isLoading(),agv.getRoute(),nextAgv.getRoute().get(0),nextAgv.getRoute().get(1));
			}else{
				outPoint = mapInfo.getOutPoint(nextAgv.isLoading(),agv.getRoute(),currentPoint,null);
			}
			if (outPoint != null){

				nextAgv.setAlterable(false);
				nextAgv.setRoute(null);

				nextAgv.getDestination().clear();
				for (Point point : nextAgv.getStaticDestination()){
					nextAgv.getDestination().add(point);
				}

				if (nextAgv.getDestination().size()==0 || !mapInfo.isSamePoint(outPoint, nextAgv.getDestination().get(0))){
					nextAgv.getDestination().add(0,outPoint);
				}
				if (outPoint.getType().equals(PointType.Pallet)){
					outPoint.setAssignedForPod(true);
				}

				nextAgv.setStopByAgvCode(null);
				nextAgv.setStopByPodCode(null);
				nextAgv.setState(AgvState.Working);
				hasChanged = true;

			}
		}
		return hasChanged;
	}
	
	public void moveAgv(Agv agv){

		Point currentPoint = getPoint(agv.getX(), agv.getY());
		Point nextPoint = agv.getRoute().get(1);

		if (!mapInfo.isSamePoint(currentPoint,nextPoint)){
			agv.addRunDistance();
		}

		// 占用和释放点
		agv.getRoute().remove(0);
		currentPoint.setOccupiedAgvCode(null);
		currentPoint.setOccupied(false);
		nextPoint.setOccupied(true);
		nextPoint.setOccupiedAgvCode(agv.getAgvCode());
		if (agv.isLoading()){
			Pod pod = getPods().get(agv.getPodCode());
			currentPoint.setOccupiedByPod(false);
			currentPoint.setOccupiedPodCode(null);
			nextPoint.setOccupiedByPod(true);
			nextPoint.setOccupiedPodCode(pod.getPodCode());

			//要对block进行修改
			if (currentPoint.getType().equals(PointType.Pallet)){
				Block b = mapInfo.getBlocks().get(currentPoint.getBlockId());
				b.getContainPod().remove(Integer.valueOf(pod.getPodCode()));
				b.getPodCode().remove(Integer.valueOf(pod.getPodCode()));
				b.getEmptyPodLocation().add(getPoint(pod.getX(),pod.getY()));
				b.getBoard()[pod.getX() - b.getBlockStart().getX()][pod.getY() - b.getBlockStart().getY()] = 0;
				b.setOccupyRatio(b.getContainPod().size()/(b.getColNum() * b.getRowNum()));
			}
			if (nextPoint.getType().equals(PointType.Pallet)){
				Block b = mapInfo.getBlocks().get(nextPoint.getBlockId());
				b.getContainPod().put(Integer.valueOf(pod.getPodCode()),pod);
				b.getPodCode().add(Integer.valueOf(pod.getPodCode()));
				b.getEmptyPodLocation().remove(getPoint(nextPoint.getX(),nextPoint.getY()));
				b.getBoard()[nextPoint.getX() - b.getBlockStart().getX()][nextPoint.getY() - b.getBlockStart().getY()] = Integer.valueOf(pod.getPodCode());
				b.setOccupyRatio(b.getContainPod().size()/(b.getColNum() * b.getRowNum()));
			}


			pod.setBelongsBlockId(nextPoint.getBlockId());
			pod.setX(nextPoint.getX());
			pod.setY(nextPoint.getY());
		}
		
		// 设置AGV新位置
		agv.setX(nextPoint.getX());
		agv.setY(nextPoint.getY());
		
		// AGV状态还原
		agv.setStopByAgvCode(null);
		agv.setStopByPodCode(null);

		if (agv.getRoute().size()<=1){
			agv.setRoute(null);
			finishRoute(agv);
		}
	}

	public void arrivePod (Agv agv, Task task, Pod pod, Point currentPoint){
		agv.setLoading(true);// 扛起货物

		pod.setLoading(true);

		agv.getTasks().get(0).addCarryTimes();
		totalCarryTimes ++;

		pod.setAgvCode(agv.getAgvCode());
		agv.setPodCode(pod.getPodCode());
	}

	public void arriveDestination (Agv agv,Point currentPoint){
		Pod pod = Pods.get(agv.getPodCode());
		pod.setAgvCode(null);
		pod.setLoading(false);
		pod.setAssigned(false);
		pod.setAssignedAgvCode(null);

		currentPoint.setOccupiedByPod(true);
		currentPoint.setAssignedForPod(false);

		agv.setLoading(false);// 放下货物
		agv.setPodCode(null);

		agv.getTasks().get(0).addCarryTimes();
		totalCarryTimes ++;
	}


	public void finishRoute (Agv agv){
		Point currentPoint = getPoint(agv.getX(), agv.getY());
		if (agv.getStaticDestination().size() > 0 && mapInfo.isSamePoint(currentPoint, agv.getStaticDestination().get(0))){
			// 到达任务点才将任务点删去
			agv.getStaticDestination().remove(0);
			Task task = agv.getTasks().get(0);
				//情况1：在执行最后一个任务
				if (agv.getStaticDestination().size() == 0) {
					// 如果任务完成
					Pod pod = Pods.get(agv.getPodCode());
					pod.setAssigned(false);
					pod.setAssignedAgvCode(null);
					pod.setAgvCode(null);
					pod.setLoading(false);
					pod.setX(currentPoint.getX());
					pod.setY(currentPoint.getY());

					agv.getTasks().get(0).addCarryTimes();
					totalCarryTimes ++;

					if (pod.getPodCode().equals(agv.getTasks().get(0).getPodCode())){
						agv.getTasks().get(0).setFinishTaskTime(getTotalTime());
						taskStatisticData.add(agv.getTasks().get(0).getStatisticData());
					}

					currentPoint.setAssignedForPod(false);
					currentPoint.setOccupiedByPod(true);

					agv.setLoading(false);// 放下货物
					agv.setPodCode(null);



					agv.getTasks().clear();

				} else {
					if (!agv.isLoading()) {
						// 到达任务起点
						Pod pod = Pods.get(currentPoint.getOccupiedPodCode());
						if (pod == null){
							System.out.println("No pod here");
						}
						else{
							arrivePod(agv,task,pod,currentPoint);
						}
					} else if (currentPoint.getType().equals(PointType.Pallet) && agv.isLoading()){
						// 到达任务终点
						arriveDestination(agv,currentPoint);
					}
				}

		}
		else if (getPoint(agv.getX(),agv.getY()).isAssignedForPod()){
			getPoint(agv.getX(),agv.getY()).setAssignedForPod(false);
		}
		if (agv.getDestination().size() == 0){
			agv.setRoute(null);
			agv.setTasks(null);
			agv.setState(AgvState.Free);
			agv.setAlterable(true);
		}
	}
	

	public boolean writeTaskStatisticData(){
		boolean r1 = WriteData.writeData(WriteData.taskStatisticLocation, taskStatisticData);
		agvStatisticData.clear();
		agvStatisticData.add(WriteData.agvStatisticStructure);
		for(Agv agv:AGVs.values()){
			agvStatisticData.add(agv.getStatisticData());
		}
		boolean r2 = WriteData.writeData(WriteData.agvStatisticLocation, agvStatisticData);
		String statisticlIndicators = WriteData.statisticalIndicators;
		statisticlIndicators += getStatisticalIndicators();
		boolean r3 = WriteData.writeData(WriteData.StatisticalIndicatorsLocation, statisticlIndicators);
		if (r1&&r2&&r3){
			return true;
		}else{
			return false;
		}
	}
	
	public String getStatisticalIndicators(){
		int totalMoveTime = 0;
		for (Agv agv:AGVs.values()){
			totalMoveTime += agv.getRunDistance();
		}
		String s = "\n" + totalTime + "," + totalMoveTime + "," + totalCarryTimes + "," + totalWaitTime + "," + totalDeadLockTimes + "," + totalBlockingTimes + "\n\n";
		System.out.println(s);
		return s;
	}
}
