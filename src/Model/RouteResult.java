package Model;

import java.util.ArrayList;

public class RouteResult {
	private double distance = Double.MAX_VALUE;
	private ArrayList<Point> route = new ArrayList<>();
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public ArrayList<Point> getRoute() {
		return route;
	}
	public void setRoute(ArrayList<Point> route) {
		this.route = route;
	}
}
