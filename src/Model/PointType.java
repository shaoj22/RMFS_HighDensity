package Model;

import java.awt.Color;

public interface PointType {
	String Undefined = "0";   //δ����
	String CrossRoad = "1";   //������
	String WorkStation = "2"; //����̨
	String Queue = "3";       //�Ŷ���
	String TurnArea = "4";    //��ת������
	String TurnBuffer = "5";  //��ת���ܻ�����
	String Parking = "6";     //ͣ��λ
	String Charging = "7";    //�����
	String Pallet = "8";      //���ܵ�
	String Buffer = "9";      //���ܻ�����
	String Pillar = "10";     //����
	String Entrance = "11";   //�����볡��
	Color squareColor[] = {Color.GRAY, Color.WHITE, Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.PINK, new Color(250,250,160), Color.BLUE, Color.YELLOW, new Color(255, 220, 0), Color.BLACK, Color.RED};
}
