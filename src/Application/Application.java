package Application;

import MultiCooperation.MultiCooperation;
import TaskAssignment.TaskAssignment;

public class Application {
	public static void main(String[] arg){
		MultiCooperation multiCooperation = new MultiCooperation();
		TaskAssignment taskAssignment = new TaskAssignment(multiCooperation);
		taskAssignment.reLoad("data/map/generated_map.txt", "data/agv/agvData-auto.txt", "data/task/taskData-auto.txt","data/map/podData-auto.txt");
		taskAssignment.getGui().showMap();
		taskAssignment.doTask();
	}
}
