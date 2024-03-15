package Application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public interface WriteData {
	int mapNum = 3;
	int podNum = 380;
	int agvNum = 30;
	int taskNum = 1800;
	String s = "map" + String.valueOf(mapNum) + "-pod" + String.valueOf(podNum) + "-agv" + String.valueOf(agvNum) + "-task" + String.valueOf(taskNum) + "-" ;
	String taskStatisticLocation = "data/statisticData/"+ s + "taskStatisticData.csv";
	String taskStatisticStructure = "TaskCode,"
									+ "IsByWay,"
									+ "AgvCode,"
			                        + "PodCode,"
									+ "startTaskTime,"
									+ "finishTaskTime,"
									+ "totalCostTime,"
									+ "carryTimes,"
									+ "waitTime,"
									+ "deadlockTimes";

	String agvStatisticLocation = "data/statisticData/" + s + "agvStatisticData.csv";
	String agvStatisticStructure = "AgvCode,"
									+ "RunDistance,"
									+ "RunTime";
	String StatisticalIndicatorsLocation = "data/statisticData/" + s + "StatisticalIndicators.csv";
	String statisticalIndicators = "TotalTime,"
									+ "TotalMoveTime,"
									+ "TotalCarryTimes,"
									+ "TotalWaitTime,"
									+ "TotalDeadLockTimes,"
			                        + "TotalBlockingTimes";
									
	public static boolean writeData(String location, String content){
		try{
			File filename = new File(location);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(content);
			writer.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public static boolean writeData(String location, ArrayList<String> content){
		try{
			File filename = new File(location);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			for (String c:content){
				writer.write(c);
			}
			writer.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public static boolean appendData(String location, String content){
		try{
			File filename = new File(location);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));
			writer.write(content);
			writer.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public static boolean appendData(String location, ArrayList<String> content){
		try{
			File filename = new File(location);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));
			for (String c:content){
				writer.write(c);
			}
			writer.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public static boolean clearData(String location){
		try{
			File filename = new File(location);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write("");
			writer.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}
}
