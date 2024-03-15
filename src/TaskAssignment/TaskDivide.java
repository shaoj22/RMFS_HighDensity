package TaskAssignment;

import Model.*;

import java.util.ArrayList;

public class TaskDivide {
    /**
     * 将挡住的目标货架利用走道移的别的空货位
     * 将一个任务分为几个子任务
     */
    public ArrayList<SecondLevelTask> MoveOutside(Task task, MapInfo mapInfo){
        ArrayList<SecondLevelTask> secondLevelTasks = new ArrayList<>();
        Point podLocation = mapInfo.getPoints()[task.getOriginX()][task.getOriginY()];
        Point pickStation = mapInfo.getPoints()[task.getDestinationX()][task.getDestinationY()];
        if (mapInfo.isBlocked(podLocation,pickStation)){
            int direction = mapInfo.calcDirction(podLocation);
            ArrayList<Pod> movePod = mapInfo.needMove(podLocation,direction);
            int Id = 0;
            for (Pod p : movePod){
                Point newLocation = mapInfo.findStayLocation(mapInfo.getPoints()[p.getX()][p.getY()],p,direction);
                if (newLocation == null){
                    for (int i = 0; i < mapInfo.getRowNum(); i++){
                        for (int j = 0; j < mapInfo.getColNum(); j++){
                            mapInfo.getPoints()[i][j].setAssignedForPod(false);
                        }
                    }
                    return null;
                }

                newLocation.setAssignedForPod(true);
                int[] newTask = {p.getX(),p.getY(),newLocation.getX(),newLocation.getY()};
                SecondLevelTask secondLevelTask = new SecondLevelTask(newTask);
                secondLevelTask.setPodCode(p.getPodCode());
                secondLevelTask.setSecondLevelTaskCode(Integer.toString(Id));
                secondLevelTasks.add(secondLevelTask);
                Id ++;
            }
            //最后一个任务是将货架运到拣选站再运回来
            int[] finalTask = {podLocation.getX(),podLocation.getY(),pickStation.getX(),pickStation.getY()};
            SecondLevelTask secondLevelTask = new SecondLevelTask(finalTask);
            secondLevelTask.setPodCode(podLocation.getOccupiedPodCode());
            secondLevelTask.setSecondLevelTaskCode(Integer.toString(Id));
            secondLevelTasks.add(secondLevelTask);
        }
        else{
            int[] finalTask = {podLocation.getX(),podLocation.getY(),pickStation.getX(),pickStation.getY()};
            SecondLevelTask secondLevelTask = new SecondLevelTask(finalTask);
            secondLevelTask.setPodCode(podLocation.getOccupiedPodCode());
            secondLevelTask.setSecondLevelTaskCode(Integer.toString(0));
            secondLevelTasks.add(secondLevelTask);
        }
        return secondLevelTasks;
    }
}
