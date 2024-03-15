package TaskAssignment;

import java.util.*;

import Application.LoadData;

import Gui.Gui;
import Model.*;
import MultiCooperation.MultiCooperation;
import MultiCooperation.MultiCooperationStrategy;

public class TaskAssignment {
	/**
	 * 储存所有任务信息
	 */
	private ArrayList<Task> TASKs = new ArrayList<>();
	/**
	 * 储存下一个分配的任务索引
	 */
	private int taskIndex = 0;
	/**
	 * 储存下一个分配的子任务索引
	 */
	private int secondLevelTaskIndex = 0;
	/**
	 * 图形用户界面
	 */
	private Gui gui;
	/**
	 * 订单找车，订单分配顺序不变，false：订单找车的逻辑
	 */
	private boolean notChangeFlag = true;
	/**
	 * 存储策略，false:存储到最近的点
	 */
	private boolean original = false;


	
	public TaskAssignment(MultiCooperation multiCooperation) {
		gui = new Gui(multiCooperation);
	}

	public ArrayList<Task> getTASKs() {
		return TASKs;
	}
	public void setTASKs(ArrayList<Task> tASKs) {
		TASKs = tASKs;
	}
	public Gui getGui() {
		return gui;
	}
	public void setGui(Gui gui) {
		this.gui = gui;
	}
	public boolean isOriginal() {
		return original;
	}

	public void reLoad(String mapLocation, String agvLocation, String taskLocation, String podLocation){
		// 保证多车协同的策略与下拉框中的策略相同
		String multiCooperationStrategy = MultiCooperationStrategy.DeadLockDetectionAndRecovery;
		if (gui.getMulticooperation()!=null){
			multiCooperationStrategy = gui.getMulticooperation().getMultiCooperationStrategy();
		}
		gui.setMulticooperation(new MultiCooperation());
		gui.getMulticooperation().setMultiCooperationStrategy(multiCooperationStrategy);
		TASKs.clear();
		taskIndex = 0;
		
		boolean r1 = LoadData.loadMap(mapLocation, gui.getMulticooperation());
		boolean r2 = LoadData.loadAgv(agvLocation, gui.getMulticooperation());
		boolean r4 = LoadData.loadPod(podLocation,gui.getMulticooperation());
		boolean r3 = LoadData.loadTaskList(taskLocation, this);
		gui.setTotalTaskNum(TASKs.size());
		gui.setAssignedTaskNum(0);
		gui.setCanRun((r1?"1":"0")+(r2?"1":"0")+(r3?"1":"0")+(r4?"1":"0"));
		
		gui.setReLoad(false);
		gui.repaint();
	}
	
	/**
	 * 执行任务，多车协同，更新仿真动画
	 */

	public void addDestination (Agv agv, Point start,Point end, MultiCooperation multiCooperation){
		// 对于AGV的设置
		agv.getDestination().add(start);
		agv.getDestination().add(end);
		agv.getStaticDestination().add(start);
		agv.getStaticDestination().add(end);
		if (end.getType().equals(PointType.WorkStation)){
			if (isOriginal()) {
				agv.getDestination().add(start);
				agv.getStaticDestination().add(start);
				start.setAssignedForPod(true);
			}
			else{  //寻找距离当前pickstation最近且没有被分配的空货位

				Point p = multiCooperation.getNearestPoint(end.getX() , end.getY());
				if (p == null) {
					p = start;
				}

				agv.getDestination().add(p);
				agv.getStaticDestination().add(p);
				p.setAssignedForPod(true);
			}
		}
		else{
			end.setAssignedForPod(true);
			start.setAssignedForPod(false);
		}
	}

	/**
	 * 大任务被顺路执行
	 */
	public void agvDealBywayTask (Agv agv, int taskX, int taskY, MultiCooperation multiCooperation, Pod pod){
		//扛着这个货架回去或执行任务的路上
		if (agv.isLoading() && agv.getPodCode().equals(pod.getPodCode())){
			if (agv.getStaticDestination().get(0).getX() != taskX || agv.getStaticDestination().get(0).getY() != taskY){
				agv.getStaticDestination().add(0,multiCooperation.getPoint(taskX,taskY));
			}
			agv.getDestination().clear();
			for (int i = 0; i < agv.getStaticDestination().size();i++){
				agv.getDestination().add(agv.getStaticDestination().get(i));
			}
			agv.setRoute(null);
		}
		//还没有扛着这个货架
		else {
			for (int i = 0; i < agv.getStaticDestination().size() - 1; i++){
				if (agv.getStaticDestination().get(i).isOccupiedByPod()
						&& agv.getStaticDestination().get(i).getOccupiedPodCode().equals(pod.getPodCode())){
					if (i == 0 && agv.getStaticDestination().size() == 1){
						System.out.println("problem is here");
					}
					if (agv.getStaticDestination().get(i + 1).getX() != taskX || agv.getStaticDestination().get(i + 1).getY() != taskY){
						agv.getStaticDestination().add(i+1, multiCooperation.getPoint(taskX,taskY));
						break;
					}
				}
			}
			agv.getDestination().clear();
			for (int i = 0; i < agv.getStaticDestination().size();i++){
				agv.getDestination().add(agv.getStaticDestination().get(i));
			}
			agv.setRoute(null);
		}
	}

	/**
	 * 新添加的任务是一段式任务
	 */
	public void agvDealBywaySecondLevelTask (Agv agv, SecondLevelTask slt, MultiCooperation multiCooperation){
		int taskX = slt.getDestinationX();
		int taskY = slt.getDestinationY();
		if (agv != null){
			if (agv.getStaticDestination().size() >= 1){
				Point tmp = agv.getStaticDestination().get(agv.getStaticDestination().size() - 1);
				if (tmp.getX() != taskX|| tmp.getY() != taskY){
					//判断与最后一个要执行的去是否相同，不相同的话增加任务点
					agv.getStaticDestination().add(agv.getStaticDestination().size() - 1, multiCooperation.getPoint(taskX,taskY));
					agv.getDestination().add(agv.getDestination().size() - 1, multiCooperation.getPoint(taskX,taskY));
				}
			}
		}
	}






	public void doTask(){
		MultiCooperation multiCooperation = gui.getMulticooperation();
		TaskDivide taskDivide = new TaskDivide();
		while(true){
			synchronized(gui){
				if (gui.isReLoad()){
					reLoad(gui.getMapLocation(), gui.getAgvLocation(), gui.getTaskLocation(), gui.getPodLocation());
					multiCooperation = gui.getMulticooperation();
				}
				long time1 = new Date().getTime();

				//***********按照订单来找车的逻辑**********
				// GUI的repaint函数未完成就会进入后面的循环，当刷新周期大于0.1秒时，必须保证上一帧图像刷新完成才能进入下一帧

				if (((gui.getCycle()<=100 || gui.isRepaintFinished()) &&!gui.isPauseFlag())){
					// 将任务池中的任务分配给所有小车，直到找不到空闲小车执行任务
					while (taskIndex < gui.getTotalTaskNum())
					{

						boolean flagNocar = false;
						Task task = TASKs.get(taskIndex);

                        //判断当前是否有空闲小车
                        boolean flag = false;
                        for (String agvCode:getGui().getMulticooperation().getAGVCodes()) {
                            Agv agv = getGui().getMulticooperation().getAGVs().get(agvCode);
                            if (agv.getState().equals(AgvState.Free)){
                                flag = true;
                                break;
                            }
                        }

                        if (!flag){
                            break;
                        }

						//找到离任务点最近的，且未被分配的货架
						if (task.getPodCode() == null){
							Pod targetPod = multiCooperation.findPodByLoc(task.getOriginX(),task.getOriginY());
							task.setPodCode(targetPod.getPodCode());
							int[] location = {targetPod.getX() , targetPod.getY()};
							task.setOrigin(location);
						}

						// 针对未分解的任务分解任务
						if (!task.isDivided()){

							ArrayList<SecondLevelTask> secondLevelTasks = taskDivide.MoveOutside(task,multiCooperation.getMapInfo());
							if (secondLevelTasks == null){
								task.setSecondLevelTasks(null);
							}
							else{
								task.setSecondLevelTasks(secondLevelTasks);
								task.setSecondLevelTaskNum(secondLevelTasks.size());

								task.setDivided(true);
							}
						}

						if (task.getSecondLevelTasks() == null){
							break;
						}

						//将子任务分配给所有的小车，直到找不到空闲小车执行任务
						while (secondLevelTaskIndex < task.getSecondLevelTaskNum()){
							SecondLevelTask secondLevelTask = task.getSecondLevelTasks().get(secondLevelTaskIndex);

							/*// 判断该子任务是不是能被顺路执行
							while (multiCooperation.getPods().get(secondLevelTask.getPodCode()).isAssigned() == true){
								secondLevelTaskIndex ++;
								Agv agv = multiCooperation.getAGVs().get(multiCooperation.getPods().get(secondLevelTask.getPodCode()).getAssignedAgvCode());
								// 判断该任务是三段式还是两段式
								if (!multiCooperation.getPoint(secondLevelTask.getDestinationX(),secondLevelTask.getDestinationY()).getType().equals(PointType.Pallet)){
									agvDealBywaySecondLevelTask(agv,secondLevelTask,multiCooperation);
									agv.getTaskCodeList().add(task.getTaskCode());
									agv.getTasks().add(task);
									if (agv.getTaskCodeList().indexOf(task.getTaskCode()) == -1){
										agv.getTaskCodeList().add(task.getTaskCode());
										agv.getTasks().add(task);
									}
									break;
								}
								// 将任务标记为顺路
								secondLevelTask.setByWay(true);
								secondLevelTask = task.getSecondLevelTasks().get(secondLevelTaskIndex);

							}*/

							//  3.2该子任务不能被顺路执行，为其找一辆空闲小车
							Agv agv = multiCooperation.getNearestAgv(secondLevelTask.getOriginX(),secondLevelTask.getOriginY());
							if (task.getStartTaskTime() == 0) {
								task.setStartTaskTime(multiCooperation.getTotalTime());
							}
							if (agv == null){
								flagNocar = true;
								break;
							}
							else{
								task.getAgvCode().add(agv.getAgvCode());
							}

							// 3.3将子任务分配给小车，在小车目的地队列中加上起点、终点和起点

							Pod pod = multiCooperation.getPods().get(secondLevelTask.getPodCode());
							pod.setAssigned(true);
							pod.setAssignedAgvCode(agv.getAgvCode());
							Point start = multiCooperation.getPoint(secondLevelTask.getOriginX(),secondLevelTask.getOriginY());

							if (!start.isOccupiedByPod() || (start.isOccupiedByPod() && !start.getOccupiedPodCode().equals(pod.getPodCode()))){
								//由于中间货架的搬移，可能导致一些货架的初始点发生了变化，此时需要重新定义初始点
								//不管怎么搬移，还是在同一个block中
								Block b = multiCooperation.getMapInfo().getBlocks().get(start.getBlockId());
								for (int i = 0; i < b.getRowNum(); i++){
									for (int j = 0; j < b.getColNum(); j++){
										Point newStart = multiCooperation.getPoint(b.getBlockStart().getX() + i, b.getBlockStart().getY() + j);

										if (newStart.isOccupiedByPod() && newStart.getOccupiedPodCode().equals(pod.getPodCode())){
											start = multiCooperation.getPoint(b.getBlockStart().getX() + i, b.getBlockStart().getY() + j);
											int[] newStartLocation = {b.getBlockStart().getX() + i, b.getBlockStart().getY() + j};
											secondLevelTask.setOrigin(newStartLocation);
											break;
										}
									}
								}
							}



							Point end = multiCooperation.getPoint(secondLevelTask.getDestinationX(),secondLevelTask.getDestinationY());
							addDestination(agv,start,end,multiCooperation);
							secondLevelTask.setAgvCode(agv.getAgvCode());
							secondLevelTask.setRecieveTaskTime(multiCooperation.getTotalTime());

							// 3.4设置小车开始接受任务
							agv.getTaskCodeList().add(0, task.getTaskCode());
							ArrayList<Task> tasks = new ArrayList<>();
							tasks.add(task);
							agv.setTasks(tasks);

							agv.setState(AgvState.Working);


							secondLevelTaskIndex ++;
						}
						if (secondLevelTaskIndex == task.getSecondLevelTaskNum()){
							taskIndex++;
							secondLevelTaskIndex = 0;
						}
						if (flagNocar){
							break;
						}

					}
				}
				if ((gui.getCycle()<=100 || gui.isRepaintFinished()) &&!gui.isPauseFlag()){
					multiCooperation.executeTask();
					//等待一段时间
					try{
						Thread.sleep((int)(gui.getCycle()));
					}catch(InterruptedException e){
						Thread.currentThread().interrupt();
					}
					gui.setAssignedTaskNum(taskIndex);
					long time2 = new Date().getTime();
					gui.setActualFps(1000.0/(time2-time1));
					gui.repaint(false);
				}

			}

		}
	}
}
