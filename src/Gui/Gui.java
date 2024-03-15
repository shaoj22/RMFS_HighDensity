package Gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Model.*;
import Model.Point;
import MultiCooperation.MultiCooperation;
import MultiCooperation.MultiCooperationStrategy;

public class Gui extends JPanel{
	//����GUI�ر���
	//�ֿ⣺15
	//��ݣ�30
	private int width = 15;
	//����GUIҳ�߾�
	private int leftSpace = 30;
	private int centerSpace = 30;
	private int rightSpace = 30;
	private int upperSpace = 20;
	private int bottomSpace = 50;
	private int controlSpace = 200;
	private int minHeight = 400;
	/**
	 * ����GUI���б�־
	 */
	private String canRun = "111";
	/**
	 * ����GUI��ͣ��־
	 */
	private boolean pauseFlag = true;
	/**
	 * ����ˢ������
	 */
	private double cycle = 0;
	/**
	 * ʵ��ˢ��Ƶ��
	 */
	private double actualFps;
	/**
	 * ��Ҫ��ɵ���������
	 */
	private int totalTaskNum=0;
	/**
	 * �ѷ������������
	 */
	private int assignedTaskNum=0;
	/**
	 * ���¼��ر�־
	 */
	private boolean isReLoad = false;
	/**
	 * ��ͼ��ɱ�־
	 */
	private boolean repaintFinished = true;
	/**
	 * ���¼��ص�ͼλ��
	 */
	private String mapLocation = null;
	/**
	 * ���¼���С��λ��
	 */
	private String agvLocation = null;
	/**
	 * ���¼�������λ��
	 */
	private String taskLocation = null;
	/**
	 * ���¼��ػ�����Ϣ
	 */
	private String podLocation = null;
	
	//�೵Эͬ,�洢��ͼ��AGV��Ϣ
	MultiCooperation multicooperation = new MultiCooperation();
	
	/**
	 * cycle�ؼ�
	 * @return
	 */
	private JLabel cycleLabel = new JLabel();
	
	/**
	 * fps�ؼ�
	 * @return
	 */
	private JLabel fpsLabel = new JLabel();
	/**
	 * task�ؼ�
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
	 * �������GUI�Ŀؼ�
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
		
		// �������
		JPanel controlPanel = new JPanel();
		controlPanel.setLocation(colNum*width+leftSpace+centerSpace+rightSpace, 0);
		controlPanel.setSize(controlSpace, Math.max(rowNum*width+upperSpace+bottomSpace, minHeight));
		controlPanel.setBackground(Color.WHITE);
		controlPanel.setLayout(null);
		
		// ���Ͻ���ʾ��������
		cycleLabel.setLocation(10, 0);
		cycleLabel.setSize(controlSpace/2,30);
		cycleLabel.setHorizontalAlignment(JTextField.LEFT);
		controlPanel.add(cycleLabel);
		
		// ���Ͻ���ʾʵ��FPS
		fpsLabel.setLocation(controlSpace/2+20,0);
		fpsLabel.setSize(controlSpace/2-40,30);
		fpsLabel.setHorizontalAlignment(JTextField.RIGHT);
		controlPanel.add(fpsLabel);
		
		// FPSѡ���
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
		
		// ������ͣ��ť
		ImageIcon ii = new ImageIcon("images/start.jpg");
		setPauseFlag(true);
		JButton pauseButton = new JButton(ii);
		pauseButton.setLocation(controlSpace/2-20,80);
		pauseButton.setSize(ii.getIconWidth(), ii.getIconHeight());
		//ȥ���߿�
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
		
		// ���Ͻ���ʾʵ��FPS
		taskLabel.setLocation(10,110);
		taskLabel.setSize(controlSpace-20,30);
		taskLabel.setHorizontalAlignment(JTextField.RIGHT);
		controlPanel.add(taskLabel);

		//�������pod
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
								// 0��9��ASCII���Ӧֵ
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
								// ��������Pod����
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

		
		// �����������
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
								// 0��9��ASCII���Ӧֵ
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
								// ��������������
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
		
		// �������С��
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
								// 0��9��ASCII���Ӧֵ
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
								// ��������������
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
		
		// ���¼������ݰ�ť
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
		
		// ����ͳ�����ݰ�ť
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
		
		// Strategyѡ���
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
		super.paint(g);//���Ӵ˾�Ļ����еİ�ť��Ҫ��껮������ʾ
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
	 * �����ͼ����ͼ���ò�ͬ��ɫ�����ر����ͣ��õر�ָ�����߶α�ʾ�ر����ڱ�����ӹ�ϵ
	 * @param g
	 */
	public void paintLayout(Graphics g) {
		MapInfo mapInfo = multicooperation.getMapInfo();
		int rowNum = mapInfo.getRowNum();
		int colNum = mapInfo.getColNum();
		Point [][] points = mapInfo.getPoints();
		//���ر���ɫ����ͬ������ɫ��ͬ
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
		
		//����ɫ�߿�
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
		
		//�Ӽ�ͷ��û�м��أ�ֻ�м�����ʾ����
		/*for (int i = 0; i < rowNum; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				int centerX = leftSpace+j*width+width/2;
				int centerY = upperSpace+i*width+width/2;
				
				//���ݵر���������·��������ɫ�����ر�Ϊ���ܣ�����·����ɫΪ�̣�����Ϊ��
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
	 * ���AGV����ͼ����ɫԲ��ʾAGVæµ,��ɫԲ��ʾAGV����
	 * @param g
	 */
	public void paintAgv(Graphics g) {
		MapInfo mapInfo = multicooperation.getMapInfo();
		int rowNum = mapInfo.getRowNum();
		int colNum = mapInfo.getColNum();
		Point [][] sq = mapInfo.getPoints();
		//AGV����ͼ�ڲ���ͼ�Ҳ�չʾ������ƫ�ƾ�������
		int offset = colNum*width+centerSpace;
		//���ر���ɫ������������;�Ϊ��ɫ��δ��������Ϊ��ɫ��Ϊδʹ������
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
		
		//���߿�
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
		
		//��AGVλ��
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
