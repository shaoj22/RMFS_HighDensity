package Model;

import java.awt.Color;

public interface PointType {
	String Undefined = "0";   //未定义
	String CrossRoad = "1";   //过道点
	String WorkStation = "2"; //工作台
	String Queue = "3";       //排队区
	String TurnArea = "4";    //旋转货架区
	String TurnBuffer = "5";  //旋转货架缓冲区
	String Parking = "6";     //停车位
	String Charging = "7";    //充电区
	String Pallet = "8";      //货架点
	String Buffer = "9";      //货架缓存区
	String Pillar = "10";     //柱子
	String Entrance = "11";   //货架入场点
	Color squareColor[] = {Color.GRAY, Color.WHITE, Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.PINK, new Color(250,250,160), Color.BLUE, Color.YELLOW, new Color(255, 220, 0), Color.BLACK, Color.RED};
}
