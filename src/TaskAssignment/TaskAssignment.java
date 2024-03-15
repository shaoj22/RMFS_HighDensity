package TaskAssignment;

import java.util.*;

import Application.LoadData;

import Gui.Gui;
import Model.*;
import MultiCooperation.MultiCooperation;
import MultiCooperation.MultiCooperationStrategy;

public class TaskAssignment {
	/**
	 * ��������������Ϣ
	 */
	private ArrayList<Task> TASKs = new ArrayList<>();
	/**
	 * ������һ���������������
	 */
	private int taskIndex = 0;
	/**
	 * ������һ�����������������
	 */
	private int secondLevelTaskIndex = 0;
	/**
	 * ͼ���û�����
	 */
	private Gui gui;
	/**
	 * �����ҳ�����������˳�򲻱䣬false�������ҳ����߼�
	 */
	private boolean notChangeFlag = true;
	/**
	 * �洢���ԣ�false:�洢������ĵ�
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
		// ��֤�೵Эͬ�Ĳ������������еĲ�����ͬ
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
	 * ִ�����񣬶೵Эͬ�����·��涯��
	 */

	public void addDestination (Agv agv, Point start,Point end, MultiCooperation multiCooperation){
		// ����AGV������
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
			else{  //Ѱ�Ҿ��뵱ǰpickstation�����û�б�����Ŀջ�λ

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
	 * ������˳·ִ��
	 */
	public void agvDealBywayTask (Agv agv, int taskX, int taskY, MultiCooperation multiCooperation, Pod pod){
		//����������ܻ�ȥ��ִ�������·��
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
		//��û�п����������
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
	 * ����ӵ�������һ��ʽ����
	 */
	public void agvDealBywaySecondLevelTask (Agv agv, SecondLevelTask slt, MultiCooperation multiCooperation){
		int taskX = slt.getDestinationX();
		int taskY = slt.getDestinationY();
		if (agv != null){
			if (agv.getStaticDestination().size() >= 1){
				Point tmp = agv.getStaticDestination().get(agv.getStaticDestination().size() - 1);
				if (tmp.getX() != taskX|| tmp.getY() != taskY){
					//�ж������һ��Ҫִ�е�ȥ�Ƿ���ͬ������ͬ�Ļ����������
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

				//***********���ն������ҳ����߼�**********
				// GUI��repaint����δ��ɾͻ��������ѭ������ˢ�����ڴ���0.1��ʱ�����뱣֤��һ֡ͼ��ˢ����ɲ��ܽ�����һ֡

				if (((gui.getCycle()<=100 || gui.isRepaintFinished()) &&!gui.isPauseFlag())){
					// ��������е�������������С����ֱ���Ҳ�������С��ִ������
					while (taskIndex < gui.getTotalTaskNum())
					{

						boolean flagNocar = false;
						Task task = TASKs.get(taskIndex);

                        //�жϵ�ǰ�Ƿ��п���С��
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

						//�ҵ������������ģ���δ������Ļ���
						if (task.getPodCode() == null){
							Pod targetPod = multiCooperation.findPodByLoc(task.getOriginX(),task.getOriginY());
							task.setPodCode(targetPod.getPodCode());
							int[] location = {targetPod.getX() , targetPod.getY()};
							task.setOrigin(location);
						}

						// ���δ�ֽ������ֽ�����
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

						//���������������е�С����ֱ���Ҳ�������С��ִ������
						while (secondLevelTaskIndex < task.getSecondLevelTaskNum()){
							SecondLevelTask secondLevelTask = task.getSecondLevelTasks().get(secondLevelTaskIndex);

							/*// �жϸ��������ǲ����ܱ�˳·ִ��
							while (multiCooperation.getPods().get(secondLevelTask.getPodCode()).isAssigned() == true){
								secondLevelTaskIndex ++;
								Agv agv = multiCooperation.getAGVs().get(multiCooperation.getPods().get(secondLevelTask.getPodCode()).getAssignedAgvCode());
								// �жϸ�����������ʽ��������ʽ
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
								// ��������Ϊ˳·
								secondLevelTask.setByWay(true);
								secondLevelTask = task.getSecondLevelTasks().get(secondLevelTaskIndex);

							}*/

							//  3.2���������ܱ�˳·ִ�У�Ϊ����һ������С��
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

							// 3.3������������С������С��Ŀ�ĵض����м�����㡢�յ�����

							Pod pod = multiCooperation.getPods().get(secondLevelTask.getPodCode());
							pod.setAssigned(true);
							pod.setAssignedAgvCode(agv.getAgvCode());
							Point start = multiCooperation.getPoint(secondLevelTask.getOriginX(),secondLevelTask.getOriginY());

							if (!start.isOccupiedByPod() || (start.isOccupiedByPod() && !start.getOccupiedPodCode().equals(pod.getPodCode()))){
								//�����м���ܵİ��ƣ����ܵ���һЩ���ܵĳ�ʼ�㷢���˱仯����ʱ��Ҫ���¶����ʼ��
								//������ô���ƣ�������ͬһ��block��
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

							// 3.4����С����ʼ��������
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
					//�ȴ�һ��ʱ��
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
