package Model;

import java.util.*;

public class Block {
    private  int blockId;
    private int rowNum;
    private int colNum;
    //Block中货架所占比例
    private double occupyRatio;
    //Block中货架编号数组，0表示空货位
    private  int[][] board;
    //Block左上角的点
    private Point blockStart ;

    private ArrayList<Point> emptyPodLocation = new ArrayList<>();
    private HashSet<Integer> podCode = new HashSet<>();
    private HashMap<Integer,Pod> containPod = new HashMap<>();

    public int getRowNum() {
        return rowNum;
    }
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public int getColNum() {
        return colNum;
    }
    public void setColNum(int colNum) {
        this.colNum = colNum;
    }
    public double getOccupyRatio() {
        return occupyRatio;
    }
    public void setOccupyRatio(double occupyRatio) {
        this.occupyRatio = occupyRatio;
    }
    public int[][] getBoard() {
        return board;
    }
    public void setBoard(int[][] board) {
        this.board = board;
    }
    public Point getBlockStart() {
        return blockStart;
    }
    public void setBlockStart(Point blockStart) {
        this.blockStart = blockStart;
    }
    public int getBlockId() {
        return blockId;
    }
    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
    public ArrayList<Point> getEmptyPodLocation() {
        return emptyPodLocation;
    }
    public void setEmptyPodLocation(ArrayList<Point> emptyPodLocation) {
        this.emptyPodLocation = emptyPodLocation;
    }
    public HashSet<Integer> getPodCode() {
        return podCode;
    }
    public void setPodCode(HashSet<Integer> podCode) {
        this.podCode = podCode;
    }
    public HashMap<Integer, Pod> getContainPod() {
        return containPod;
    }
    public void setContainPod(HashMap<Integer, Pod> containPod) {
        this.containPod = containPod;
    }
}
