package Gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Model.*;
import Model.Point;
import MultiCooperation.MultiCooperation;
import MultiCooperation.MultiCooperationStrategy;

public class Gui extends JPanel{
	//设置GUI地标宽度
	//仓库：15
	//快递：30
	private int width = 15;
	//设置GUI页边距
	private int leftSpace = 30;
	private int centerSpace = 30;
	private int rightSpace = 30;
	private int upperSpace = 20;
	private int bottomSpace = 50;
	private int controlSpace = 200;
	private int minHeight = 400;
	/**
	 * 设置GUI可行标志
	 */
	private String canRun = "111";
	/**
	 * 设置GUI暂停标志
	 */
	private boolean pauseFlag = true;
	/**
	 * 设置刷新周期
	 */
	private double cycle = 0;
	/**
	 * 实际刷新频率
	 */
	private double actualFps;
	/**
	 * 需要完成的任务总数
	 */
	private int totalTaskNum=0;
	/**
	 * 已分配的任务总数
	 */
	private int assignedTaskNum=0;
	/**
	 * 重新加载标志
	 */
	private boolean isReLoad = false;
	/**
	 * 绘图完成标志
	 */
	private boolean repaintFinished = true;
	/**
	 * 重新加载地图位置
	 */
	private String mapLocation = null;
	/**
	 * 重新加载小车位置
	 */
	private String agvLocation = null;
	/**
	 * 重新加载任务位置
	 */
	private String taskLocation = null;
	/**
	 * 重新加载货架信息
	 */
	private String podLocation = null;
	
	//多车协同,存储地图和AGV信息
	MultiCooperation multicooperation = new MultiCooperation();
	
	/**
	 * cycle控件
	 * @return
	 */
	private JLabel cycleLabel = new JLabel();
	
	/**
	 * fps控件
	 * @return
	 */
	private JLabel fpsLabel = new JLabel();
	/**
	 * task控件
	 * @return
	 */
	private JLabel taskLabel = new JLabel();
	
	
	public String getCanRun() {
		return canRun;
	}
	public void setCanRun(String canRun) {
		this.canRun = canRun;
	}
	public boolean isPauseFlag() {
		return pauseFlag;
	}
	public void setPauseFlag(boolean pauseFlag) {
		this.pauseFlag = pauseFlag;
	}
	public double getCycle() {
		return cycle;
	}
	public void setCycle(double cycle) {
		this.cycle = cycle;
	}
	public double getActualFps() {
		return actualFps;
	}
	public void setActualFps(double actualFps) {
		this.actualFps = actualFps;
	}
	public MultiCooperation getMulticooperation() {
		return multicooperation;
	}
	public void setMulticooperation(MultiCooperation multicooperation) {
		this.multicooperation = multicooperation;
	}
	public int getTotalTaskNum() {
		return totalTaskNum;
	}
	public void setTotalTaskNum(int totalTaskNum) {
		this.totalTaskNum = totalTaskNum;
	}
	public int getAssignedTaskNum() {
		return assignedTaskNum;
	}
	public void setAssignedTaskNum(int assignedTaskNum) {
		this.assignedTaskNum = assignedTaskNum;
	}
	public boolean isReLoad() {
		return isReLoad;
	}
	public void setReLoad(boolean isReLoad) {
		this.isReLoad = isReLoad;
	}
	public boolean isRepaintFinished() {
		return repaintFinished;
	}
	public void setRepaintFinished(boolean repaintFinished) {
		this.repaintFinished = repaintFinished;
	}
	public String getMapLocation() {
		return mapLocation;
	}
	public void setMapLocation(String mapLocation) {
		this.mapLocation = mapLocation;
	}
	public String getAgvLocation() {
		return agvLocation;
	}
	public void setAgvLocation(String agvLocation) {
		this.agvLocation = agvLocation;
	}
	public String getTaskLocation() {
		return taskLocation;
	}
	public void setTaskLocation(String taskLocation) {
		this.taskLocation = taskLocation;
	}
	public String getPodLocation() {
		return  podLocation;
	}
	public void setPodLocation(String podLocation){
		this.podLocation = podLocation;
	}
	public Gui (MultiCooperation multicooperation1) {
		setMulticooperation(multicooperation1);
	}
	
	/**
	 * 定义各种GUI的控件
	 */
	public void showMap()
	{
		MapInfo mapInfo = multicooperation.getMapInfo();
		int rowNum = mapInfo.getRowNum();
		int colNum = mapInfo.getColNum();
		JFrame jFrame = new JFrame("Agv Simulation");
		jFrame.setLayout(null);
		jFrame.setSize(colNum*width+leftSpace+centerSpace+rightSpace+controlSpace, Math.max(rowNum*width+upperSpace+bottomSpace, minHeight));
		jFrame.setLocationRelativeTo(null);
		jFrame.setResizable(false);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLocation(0, 0);
		this.setSize(colNum*width+leftSpace+centerSpace, Math.max(rowNum*width+upperSpace+bottomSpace, minHeight));
		this.setBackground(new Color(252, 230, 201));
		this.setLayout(null);
		
		// 控制面板
		JPanel controlPanel = new JPanel();
		controlPanel.setLocation(colNum*width+leftSpace+centerSpace+rightSpace, 0);
		controlPanel.setSize(controlSpace, Math.max(rowNum*width+upperSpace+bottomSpace, minHeight));
		controlPanel.setBackground(Color.WHITE);
		controlPanel.setLayout(null);
		
		// 左上角显示总周期数
		cycleLabel.setLocation(10, 0);
		cycleLabel.setSize(controlSpace/2,30);
		cycleLabel.setHorizontalAlignment(JTextField.LEFT);
		controlPanel.add(cycleLabel);
		
		// 右上角显示实际FPS
		fpsLabel.setLocation(controlSpace/2+20,0);
		fpsLabel.setSize(controlSpace/2-40,30);
		fpsLabel.setHorizontalAlignment(JTextField.RIGHT);
		controlPanel.add(fpsLabel);
		
		// FPS选择框
		JLabel fpsText = new JLabel("Refresh Cycle(ms):");
		fpsText.setLocation(10, 40);
		fpsText.setSize(150, 30);
		controlPanel.add(fpsText);
		JComboBox fpsBox = new JComboBox();
		fpsBox.setLocation(controlSpace-70, 40);
		fpsBox.setSize(60, 30);
		fpsBox.addItem(0);
		fpsBox.addItem(1);
		fpsBox.addItem(10);
		fpsBox.addItem(100);
		fpsBox.addItem(150);
		fpsBox.addItem(200);
		fpsBox.addItem(250);
		fpsBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				synchronized(this){
					setCycle(Double.parseDouble(fpsBox.getSelectedItem().toString()));
				}
			}
		});
		controlPanel.add(fpsBox);
		
		// 继续暂停按钮
		ImageIcon ii = new ImageIcon("images/start.jpg");
		setPauseFlag(true);
		JButton pauseButton = new JButton(ii);
		pauseButton.setLocation(controlSpace/2-20,80);
		pauseButton.setSize(ii.getIconWidth(), ii.getIconHeight());
		//去掉边框
		pauseButton.setBorder(null);
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					boolean mapReady = (canRun.charAt(0)=='1');
					boolean agvReady = (canRun.charAt(1)=='1');
					boolean taskReady = (canRun.charAt(2)=='1');
					if (mapReady&&agvReady&&taskReady){
						setPauseFlag(!pauseFlag);
						if (pauseFlag){
							ImageIcon ii = new ImageIcon("images/start.jpg");
							pauseButton.setIcon(ii);
						}else{
							ImageIcon ii = new ImageIcon("images/pause.jpg");
							pauseButton.setIcon(ii);
						}
					}else{
						JOptionPane.showMessageDialog(null, String.format("Error Exists in data [%s%s%s], Please check and RELOAD!", mapReady?"":" Map ", agvReady?"":" Agv ", taskReady?"":" Task "), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		controlPanel.add(pauseButton);
		
		// 右上角显示实际FPS
		taskLabel.setLocation(10,110);
		taskLabel.setSize(controlSpace-20,30);
		taskLabel.setHorizontalAlignment(JTextField.RIGHT);
		controlPanel.add(taskLabel);

		//随机生成pod
		JButton podGeneratorButton = new JButton("Pod Generator");
		podGeneratorButton.setLocation(controlSpace/2-80, 150);
		podGeneratorButton.setSize(160, 30);
		podGeneratorButton.setBorder(BorderFactory.createRaisedBevelBorder());
		podGeneratorButton.setBackground(Color.LIGHT_GRAY);
		podGeneratorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if (!pauseFlag){
						JOptionPane.showMessageDialog(null, "Please pause first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else if (multicooperation == null){
						JOptionPane.showMessageDialog(null, "Please load a map first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						String inputValue = JOptionPane.showInputDialog("Please input the Pod number!");
						if (inputValue!=null){
							boolean formalFormat = true;
							for (int i = 0; i < inputValue.length(); i++){
								int c = (int)inputValue.charAt(i);
								// 0到9的ASCII码对应值
								if (c<48||c>57){
									formalFormat = false;
									break;
								}
							}
							int value = -1;
							try{
								value = Integer.parseInt(inputValue);
							}catch(Exception ex){
								formalFormat = false;
							}
							if (formalFormat){
								// 调用生成Pod函数
								mapInfo.generatePod(value,"data/map/podData-auto.txt");
								JOptionPane.showMessageDialog(null, "Generate Pods Succeed!", "Information", JOptionPane.INFORMATION_MESSAGE);
							}else{
								JOptionPane.showMessageDialog(null, "Please input a number between 0 and 2147483647!", "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		});
		controlPanel.add(podGeneratorButton);

		
		// 随机生成任务
		JButton taskGeneratorButton = new JButton("Task Generator");
		taskGeneratorButton.setLocation(controlSpace/2-80, 200);
		taskGeneratorButton.setSize(160, 30);
		taskGeneratorButton.setBorder(BorderFactory.createRaisedBevelBorder());
		taskGeneratorButton.setBackground(Color.LIGHT_GRAY);
		taskGeneratorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if (!pauseFlag){
						JOptionPane.showMessageDialog(null, "Please pause first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else if (multicooperation == null){
						JOptionPane.showMessageDialog(null, "Please load a map first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						String inputValue = JOptionPane.showInputDialog("Please input the task number!");
						if (inputValue!=null){
							boolean formalFormat = true;
							for (int i = 0; i < inputValue.length(); i++){
								int c = (int)inputValue.charAt(i);
								// 0到9的ASCII码对应值
								if (c<48||c>57){
									formalFormat = false;
									break;
								}
							}
							int value = -1;
							try{
								value = Integer.parseInt(inputValue);
							}catch(Exception ex){
								formalFormat = false;
							}
							if (formalFormat){
								// 调用生成任务函数
								mapInfo.generateTask(value, "data/task/taskData-auto.txt");
								JOptionPane.showMessageDialog(null, "Generate Tasks Succeed!", "Information", JOptionPane.INFORMATION_MESSAGE);
							}else{
								JOptionPane.showMessageDialog(null, "Please input a number between 0 and 2147483647!", "Error", JOptionPane.ERROR_MESSAGE); 
							}
						}
					}
				}
			}
		});
		controlPanel.add(taskGeneratorButton);
		
		// 随机生成小车
		JButton agvGeneratorButton = new JButton("Agv Generator");
		agvGeneratorButton.setLocation(controlSpace/2-80, 250);
		agvGeneratorButton.setSize(160, 30);
		agvGeneratorButton.setBorder(BorderFactory.createRaisedBevelBorder());
		agvGeneratorButton.setBackground(Color.LIGHT_GRAY);
		agvGeneratorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if (!pauseFlag){
						JOptionPane.showMessageDialog(null, "Please pause first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else if (multicooperation == null){
						JOptionPane.showMessageDialog(null, "Please load a map first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						String inputValue = JOptionPane.showInputDialog("Please input the agv number!");
						if (inputValue!=null){
							boolean formalFormat = true;
							for (int i = 0; i < inputValue.length(); i++){
								int c = (int)inputValue.charAt(i);
								// 0到9的ASCII码对应值
								if (c<48||c>57){
									formalFormat = false;
									break;
								}
							}
							int value = -1;
							try{
								value = Integer.parseInt(inputValue);
							}catch(Exception ex){
								formalFormat = false;
							}
							if (formalFormat){
								// 调用生成任务函数
								mapInfo.generateAgv(value, "data/agv/agvData-auto.txt");
								JOptionPane.showMessageDialog(null, "Generate AGVs Succeed!", "Information", JOptionPane.INFORMATION_MESSAGE);
							}else{
								JOptionPane.showMessageDialog(null, "Please input a number between 0 and 2147483647!", "Error", JOptionPane.ERROR_MESSAGE); 
							}
						}
					}
				}
			}
		});
		controlPanel.add(agvGeneratorButton);
		
		// 重新加载数据按钮
		JButton reLoadButton = new JButton("ReLoad");
		reLoadButton.setLocation(controlSpace/2-80, 300);
		reLoadButton.setSize(160, 30);
		reLoadButton.setBorder(BorderFactory.createRaisedBevelBorder());
		reLoadButton.setBackground(Color.LIGHT_GRAY);
		reLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if (!isPauseFlag()){
						JOptionPane.showMessageDialog(null, "Please pause first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						isReLoad = true;
						mapLocation = "data/map/mapData.txt";
						agvLocation = "data/agv/agvData.txt";
						taskLocation = "data/task/taskData.txt";
						podLocation = "data/map/podData.txt";
					}
				}
			}
		});
		controlPanel.add(reLoadButton);
		
		// 导出统计数据按钮
		JButton exportButton = new JButton("Export Data");
		exportButton.setLocation(controlSpace/2-80, 350);
		exportButton.setSize(160, 30);
		exportButton.setBorder(BorderFactory.createRaisedBevelBorder());
		exportButton.setBackground(Color.LIGHT_GRAY);
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if (!isPauseFlag()){
						JOptionPane.showMessageDialog(null, "Please pause first!", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						boolean succeed = multicooperation.writeTaskStatisticData();
						if (succeed){
							JOptionPane.showMessageDialog(null, "Export Succeed!", "Information", JOptionPane.INFORMATION_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "Export Failed! Ensure that All Statistic Data Files are closed!", "Information", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		});
		controlPanel.add(exportButton);
		
		// Strategy选择框
		JLabel strategyText = new JLabel("Strategy:");
		strategyText.setLocation(10, 400);
		strategyText.setSize(70, 30);
		controlPanel.add(strategyText);
		JComboBox strategyBox = new JComboBox();
		strategyBox.setLocation(70, 400);
		strategyBox.setSize(controlSpace-80, 30);
		strategyBox.addItem(MultiCooperationStrategy.DeadLockDetectionAndRecovery);
		strategyBox.addItem(MultiCooperationStrategy.FuturePathPlanningWithRecovery);
		strategyBox.addItem(MultiCooperationStrategy.DeadlockAvoidance);
		strategyBox.addItem(MultiCooperationStrategy.FuturePathPlanningWithAvoidance);
		strategyBox.addItem(MultiCooperationStrategy.HistoryBasedPathPlanning);
		strategyBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				synchronized(this){
					multicooperation.setMultiCooperationStrategy(strategyBox.getSelectedItem().toString());
				}
			}
		});
		controlPanel.add(strategyBox);
		
		jFrame.add(controlPanel);
		jFrame.add(this);
		this.setVisible(true);
		jFrame.setVisible(true);
	}
	
	public void repaint(boolean repaintFinished){
		this.repaintFinished = repaintFinished;
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);//不加此句的话，有的按钮需要鼠标划过才显示
		cycleLabel.setText(String.format("Cycles:%d", multicooperation.getTotalTime()));
		if (Double.isInfinite(actualFps)){
			fpsLabel.setText(String.format("Inf FPS"));
		}else{
			fpsLabel.setText(String.format("%.1f FPS", actualFps));
		}
		taskLabel.setText(assignedTaskNum+"/"+totalTaskNum);
		paintLayout(g);
		paintAgv(g);
		this.repaintFinished = true;
	}
	
	/**
	 * 绘出地图布局图，用不同颜色描述地标类型，用地标指出的线段表示地标与邻标的连接关系
	 * @param g
	 */
	public void paintLayout(Graphics g) {
		MapInfo mapInfo = multicooperation.getMapInfo();
		int rowNum = mapInfo.getRowNum();
		int colNum = mapInfo.getColNum();
		Point [][] points = mapInfo.getPoints();
		//填充地标颜色：不同类型颜色不同
		for (int i = 0; i < rowNum; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				if (points[i][j].isOccupiedByPod()){
					g.setColor(PointType.squareColor[8]);
					g.fillRect(leftSpace+j*width, upperSpace+i*width, width, width);
				}
				else if (!points[i][j].isOccupiedByPod() && points[i][j].getType().equals(PointType.Pallet)){
					g.setColor(PointType.squareColor[1]);
					g.fillRect(leftSpace+j*width, upperSpace+i*width, width, width);
				}
				else{
					g.setColor(PointType.squareColor[Integer.valueOf(points[i][j].getType())]);
					g.fillRect(leftSpace+j*width, upperSpace+i*width, width, width);
				}
			}
		}
		
		//画黑色边框
		g.setColor(Color.BLACK);
		g.drawLine(leftSpace, upperSpace, leftSpace, rowNum*width+upperSpace);
		for (int i = 0; i < colNum; i++)
		{
			g.drawLine(leftSpace+(i+1)*width, upperSpace, leftSpace+(i+1)*width, rowNum*width+upperSpace);
		}
		g.drawLine(leftSpace, upperSpace, colNum*width+leftSpace, upperSpace);
		for (int i = 0; i < rowNum; i++)
		{
			g.drawLine(leftSpace, upperSpace+(i+1)*width, colNum*width+leftSpace, upperSpace+(i+1)*width);
		}
		
		//加箭头：没有箭簇，只有箭柄表示方向
		/*for (int i = 0; i < rowNum; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				int centerX = leftSpace+j*width+width/2;
				int centerY = upperSpace+i*width+width/2;
				
				//根据地标类型设置路径方向颜色，若地标为货架，设置路径颜色为绿，否则为红
				String type = points[i][j].getType();
				if (type.equals(PointType.Pallet)||type.equals(PointType.Parking)||type.equals(PointType.Buffer)||type.equals(PointType.Charging)) g.setColor(Color.GREEN);
				else g.setColor(Color.RED);
				
				if (points[i][j].isUp()) {g.drawLine(centerX, centerY, centerX, centerY-width/2);}
				if (points[i][j].isDown()) {g.drawLine(centerX, centerY, centerX, centerY+width/2);}
				if (points[i][j].isLeft()) {g.drawLine(centerX, centerY, centerX-width/2, centerY);}
				if (points[i][j].isRight()) {g.drawLine(centerX, centerY, centerX+width/2, centerY);}
			}
		}*/
	}
	
	/**
	 * 绘出AGV仿真图，红色圆表示AGV忙碌,蓝色圆表示AGV空闲
	 * @param g
	 */
	public void paintAgv(Graphics g) {
		MapInfo mapInfo = multicooperation.getMapInfo();
		int rowNum = mapInfo.getRowNum();
		int colNum = mapInfo.getColNum();
		Point [][] sq = mapInfo.getPoints();
		//AGV仿真图在布局图右侧展示，定义偏移距离如下
		int offset = colNum*width+centerSpace;
		//填充地标颜色：定义过的类型均为灰色，未定义区域为白色，为未使用区域
		for (int i = 0; i < rowNum; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				if (!mapInfo.isUsefulPoint(sq[i][j])){
					continue;
				}
				g.setColor(Color.GRAY);
				g.fillRect(leftSpace+offset+j*width, upperSpace+i*width, width, width);
			}
		}
		
		//画边框
		g.setColor(Color.BLACK);
		g.drawLine(leftSpace+offset, upperSpace, leftSpace+offset, rowNum*width+upperSpace);
		for (int i = 0; i < colNum; i++)
		{
			g.drawLine(leftSpace+offset+(i+1)*width, upperSpace, leftSpace+offset+(i+1)*width, rowNum*width+upperSpace);
		}
		g.drawLine(leftSpace+offset, upperSpace, colNum*width+leftSpace+offset, upperSpace);
		for (int i = 0; i < rowNum; i++)
		{
			g.drawLine(leftSpace+offset, upperSpace+(i+1)*width, colNum*width+leftSpace+offset, upperSpace+(i+1)*width);
		}
		
		//画AGV位置
		boolean sameMap = true;
		for (String agvCode:multicooperation.getAGVCodes())
		{
			Agv agv = multicooperation.getAGVs().get(agvCode);
			if (agv.isLoading()){
				g.setColor(new Color(210,105,30));
			}else if (AgvState.Working == agv.getState()){
				g.setColor(Color.RED);
			}else{
				g.setColor(Color.BLUE);
			}
			
			int centerX = leftSpace+agv.getY()*width+width/4;
			int centerY = upperSpace+agv.getX()*width+width/4;
			if (!sameMap){
				centerX += offset;
			}
			g.fillOval(centerX, centerY, width/2, width/2);
			g.setColor(Color.BLACK);
			g.drawString(agvCode, centerX-width/4, centerY+width/2);
		}
	}
}
