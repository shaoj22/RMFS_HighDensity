package Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import Model.*;
import MultiCooperation.MultiCooperation;
import TaskAssignment.TaskAssignment;

public interface LoadData {
	public static boolean loadMap(String mapLocation, MultiCooperation multicooperation) {
		//��ȡ����
		File filename = new File(mapLocation);
		String tempString = null;
		String [] line = null;
		int countRows = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			//��ȡ����������
			tempString = reader.readLine();
			line = tempString.split(" ");
			MapInfo mapInfo = multicooperation.getMapInfo();
			mapInfo.setRowNum(Integer.valueOf(line[0]));
			mapInfo.setColNum(Integer.valueOf(line[1]));
			
			int rowNum = mapInfo.getRowNum();
			int colNum = mapInfo.getColNum();
			mapInfo.setPoints(new Point[rowNum][colNum]);
			Point [][]points = mapInfo.getPoints();

			//��ȡ����
			while ((tempString = reader.readLine()) != null  && countRows < rowNum) {
				if (tempString.length() > 0)
				{
					line = tempString.split(" ");
					for (int countCols = 0; countCols < colNum; countCols++)
					{
						points[countRows][countCols] = new Point();//����Ԫ�س�ʼ��
						points[countRows][countCols].setType(line[countCols]);//����
						points[countRows][countCols].setX(countRows);//X����
						points[countRows][countCols].setY(countCols);//Y����
						// �����ܵ㡢����������Ϊ�ɴ�Ż��ܵĵ�
						if (PointType.Buffer.equals(line[countCols])||PointType.Pallet.equals(line[countCols])){
							points[countRows][countCols].setPalletPoint(true);
						}
						// �����ڻ��ܵ㣬��Ϊ�õ�ͼ����Ϊ�ֿ�
						if (PointType.Pallet.equals(line[countCols])){
							mapInfo.setMapType(MapType.WareHouse);
							mapInfo.getPodArea().add(points[countRows][countCols]);
						}
						// ������̨�������
						if (PointType.WorkStation.equals(line[countCols])){
							mapInfo.getWorkStations().add(points[countRows][countCols]);
						}
					}
					countRows++;
				}
			}

			//��ȡ����
			countRows = 0;

			double connect = 0;

			while ((tempString = reader.readLine()) != null && countRows < rowNum) {
				if (tempString.length() > 0)
				{
					line = tempString.split(" ");
					for (int countCols = 0; countCols < colNum; countCols++)
					{
						points[countRows][countCols].setDirection(line[countCols]);

						if (line[countCols].charAt(0) == '1')
						{
							connect ++;
						}
						if (line[countCols].charAt(1) == '1')
						{
							connect ++;
						}
						if (line[countCols].charAt(2) == '1')
						{
							connect ++;
						}
						if (line[countCols].charAt(3) == '1')
						{
							connect ++;
						}

					}
					countRows++;
				}
			}
			connect = connect/(double) (rowNum * colNum);
			System.out.println("connect is " + connect);
			//�Ե�ͼ�ĵ��ж��ǲ���,�ǲ������Ͻ�
			ArrayList<Point> blockLeftUpCorner = new ArrayList<>();
			for (int i = 0; i < rowNum; i++){
				for (int j = 0; j < colNum; j++){
					if (points[i][j].getType().equals(PointType.Pallet)){
						if (i == 0 || i == rowNum - 1 || j == 0 || j == colNum - 1){
							if (mapInfo.isCorner(points[i][j])){
								if (mapInfo.isLeftUpCorner(points[i][j])){
									blockLeftUpCorner.add(points[i][j]);
								}
							}
						}
						else if (mapInfo.isBoundry(points[i][j])){
							if (mapInfo.isCorner(points[i][j])){
								if (mapInfo.isLeftUpCorner(points[i][j])){
									blockLeftUpCorner.add(points[i][j]);
								}
							}
						}
					}
				}
			}
			//���ݽ�������м���block��ÿ��block�ĳ���
			mapInfo.setBlockNum(blockLeftUpCorner.size());
			ArrayList<Block> blocks = new ArrayList<>();
			for(Point p : blockLeftUpCorner){
				Block b = new Block();
				b.setBlockStart(p);
				b.setRowNum(0);
				b.setColNum(0);
				if (p.getX() == mapInfo.getRowNum() - 1){
					b.setRowNum(1);
				}
				else{
					for (int i = p.getX(); i < mapInfo.getRowNum(); i++){
						if (!points[i][p.getY()].getType().equals(PointType.Pallet)){
							b.setRowNum(i - p.getX());
							break;
						}
					}
					if (b.getRowNum() == 0){
						b.setRowNum(mapInfo.getRowNum() - p.getX());
					}
				}
				if (p.getY() == mapInfo.getColNum() - 1){
					b.setColNum(1);
				}
				else{
					for (int j = p.getY(); j < mapInfo.getColNum(); j++){
						if (!points[p.getX()][j].getType().equals(PointType.Pallet)){
							b.setColNum(j - p.getY());
							break;
						}
					}
					if (b.getColNum() == 0){
						b.setColNum(mapInfo.getColNum() - p.getY());
					}
				}
				blocks.add(b);
			}
			//��block�����е�λ�ö�����emptyPodLocation�У�board�ó�ȫ0����
			int Id = 0;
			for (Block b : blocks){
				int[][] board = new int[b.getRowNum()][b.getColNum()];
				b.setBoard(board);
				for (int i = b.getBlockStart().getX(); i < b.getBlockStart().getX() + b.getRowNum(); i++){
					for (int j = b.getBlockStart().getY(); j < b.getBlockStart().getY() + b.getColNum(); j++){
						b.getEmptyPodLocation().add(points[i][j]);
						points[i][j].setBlockId(Id);
					}
				}
				b.setBlockId(Id);
				mapInfo.getBlocks().put(Id,b);
				Id++;
			}
			reader.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Load map error!", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.print("Load map error!");
			return false;
		}
		return true;
	}
	
	public static boolean loadAgv(String agvLocation, MultiCooperation multicooperation) {
		//��ȡ����
		File filename = new File(agvLocation);
		String tempString = null;
		String [] line = null;
		int countRows = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			//��ȡС������
			tempString = reader.readLine();
			line = tempString.split(" ");
			int agvNum = Integer.valueOf(line[0]);
			multicooperation.getMapInfo().setAgvNum(agvNum);

			Point [][]points = multicooperation.getMapInfo().getPoints();
			//��ȡС��λ��
			countRows = 0;
			while ((tempString = reader.readLine()) != null && countRows < agvNum) {
				if (tempString.length() > 0)
				{
					line = tempString.split(" ");
					String agvCode = line[0];
					int row = Integer.valueOf(line[1]);
					int col = Integer.valueOf(line[2]);
					//������ͼ�ж�Ӧ��
					points[row][col].setOccupied(true);
					points[row][col].setOccupiedAgvCode(agvCode);
					
					Agv agv1 = new Agv(agvCode,row,col);
					multicooperation.getAGVs().put(agvCode, agv1);
					multicooperation.getAGVCodes().add(agvCode);
					
					countRows++;
				}
			}
			reader.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Load agv error!", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.print("Load agv error!");
			return false;
		}
		return true;
	}

	public static boolean loadPod(String PodLocation, MultiCooperation multicooperation) {
		//��ȡ����
		MapInfo mapInfo = multicooperation.getMapInfo();
		File filename = new File(PodLocation);
		String tempString = null;
		String [] line = null;
		int countRows = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			//��ȡ���ܸ���
			tempString = reader.readLine();
			line = tempString.split(" ");
			int podNum = Integer.valueOf(line[0]);

			Point [][]points = multicooperation.getMapInfo().getPoints();
			//��ȡ����λ��
			countRows = 0;
			ArrayList<Point> workStation = mapInfo.getWorkStations();
			double distance = 0;
			while ((tempString = reader.readLine()) != null && countRows <= podNum) {
				if (tempString.length() > 0)
				{
					line = tempString.split(" ");
					String podCode = line[0];
					int row = Integer.valueOf(line[1]);
					int col = Integer.valueOf(line[2]);
					//������ͼ�ж�Ӧ��
					if (row < mapInfo.getRowNum() && col < mapInfo.getColNum() && points[row][col].getType().equals(PointType.Pallet)){
						points[row][col].setOccupiedByPod(true);
						points[row][col].setOccupiedPodCode(podCode);

						Pod pod1 = new Pod(podCode,row,col);
						multicooperation.getPods().put(podCode, pod1);
						multicooperation.getPodCodes().add(podCode);

						//��pod��block�Ĺ�ϵ��ȷ(belongsBlockId��PodCode,PodLocation,EmptyPodLocation,occupyRatio,board)
						for (int i = 0; i < mapInfo.getBlockNum(); i++){
							Block b = mapInfo.getBlocks().get(i);
							if (b.getBlockStart().getX() <= row && row <= b.getBlockStart().getX() + b.getRowNum()
									&& b.getBlockStart().getY() <= col && col <= b.getBlockStart().getY() + b.getColNum()){
								pod1.setBelongsBlockId(b.getBlockId());
								mapInfo.getPoints()[pod1.getX()][pod1.getY()].setBlockId(b.getBlockId());
								b.getPodCode().add(Integer.valueOf(pod1.getPodCode()));
								b.getContainPod().put(Integer.valueOf(pod1.getPodCode()),pod1);
								b.getEmptyPodLocation().remove(points[row][col]);
								b.setOccupyRatio((double) b.getPodCode().size() / (b.getRowNum() * b.getColNum()));
								b.getBoard()[row - b.getBlockStart().getX()][col - b.getBlockStart().getY()] = Integer.valueOf(pod1.getPodCode());
								break;
							}
						}
						mapInfo.getPods().put(Integer.valueOf(pod1.getPodCode()),pod1);
						countRows++;
					}
					else{
						reader.close();
						return false;
					}
				}
			}
			reader.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Load pod error!", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.print("Load pod error!");
			return false;
		}
		return true;
	}

	public static boolean loadTaskList(String taskLocation, TaskAssignment taskAssignment){
		File filename = new File(taskLocation);
		String tempString = null;
		String [] line = new String[4];
		int count = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			//��ȡ������
			tempString = reader.readLine();
			line = tempString.split(" ");
			int taskNum = Integer.valueOf(line[0]);
			
			MapInfo mapInfo = taskAssignment.getGui().getMulticooperation().getMapInfo();
			while ((tempString = reader.readLine()) != null && count < taskNum) {
				if (tempString.length() > 0)
				{
					count++;
					
					line = tempString.split(" ");
					Task task = new Task(line);
					task.setTaskCode(count);
					// ���task�Ƿ��ڵ�ͼ��
					/*if (task.getDestinationX() < mapInfo.getRowNum() && task.getDestinationY() < mapInfo.getColNum()){
						Point p1 = mapInfo.getPoints()[taskAssignment.getGui().getMulticooperation().getPods().get(task.getPodCode()).getX()][taskAssignment.getGui().getMulticooperation().getPods().get(task.getPodCode()).getY()];
						Point p2 = mapInfo.getPoints()[task.getDestinationX()][task.getDestinationY()];
						Point [] twoPoints = {p1 , p2};
						for (Point p:twoPoints){
							if (!mapInfo.isUsefulPoint(p)){
								reader.close();
								return false;
							}
						}
					}else{
						reader.close();
						return false;
					}*/
					taskAssignment.getTASKs().add(task);
				}
			}
			reader.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Load task error!", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.print("Load task error!");
			return false;
		}
		return true;
	}
}
