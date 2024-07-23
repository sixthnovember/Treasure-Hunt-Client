package client.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import gamemodel.GameMap;
import map.Field;
import map.Point;
import map.Terrain;

public class PositionFinding {

	private List<Point> visitedMountains;
	private GameMap gamemap;
	private ArrayList<Point> pointsInMyHalf;
	private ArrayList<Point> pointsInEnemyHalf;

	public PositionFinding() {
		this.visitedMountains = new ArrayList<>();
		this.pointsInMyHalf = new ArrayList<>();
		this.pointsInEnemyHalf = new ArrayList<>();
	}

	public void setGameMap(GameMap gamemap) {
		this.gamemap = gamemap;
	}

	private void getPoints() {
		Point playerPosition = getPlayerPosition();
		this.pointsInMyHalf.clear();
		this.pointsInEnemyHalf.clear();
		// Map is 5 x 20
		if (getRowLength() > getColumnLength()) {
			if (playerPosition.getX() < getRowLength() / 2) {
				// Map is on left side
				for (Point p : this.gamemap.getGamemap().keySet()) {
					if (p.getX() < getRowLength() / 2) {
						this.pointsInMyHalf.add(p);
					} else {
						this.pointsInEnemyHalf.add(p);
					}
				}
			} else {
				// Map is on right side
				for (Point p : this.gamemap.getGamemap().keySet()) {
					if (p.getX() >= getRowLength() / 2) {
						this.pointsInMyHalf.add(p);
					} else {
						this.pointsInEnemyHalf.add(p);
					}
				}
			}
		} else { // Map is 10 x 10
			if (playerPosition.getY() < getColumnLength() / 2) {
				// Map is on top side
				for (Point p : this.gamemap.getGamemap().keySet()) {
					if (p.getX() < getColumnLength() / 2) {
						this.pointsInMyHalf.add(p);
					} else {
						this.pointsInEnemyHalf.add(p);
					}
				}
			} else {
				// Map is on bottom side
				for (Point p : this.gamemap.getGamemap().keySet()) {
					if (p.getX() >= getColumnLength() / 2) {
						this.pointsInMyHalf.add(p);
					} else {
						this.pointsInEnemyHalf.add(p);
					}
				}
			}
		}

	}

	private int getRowLength() {
		int rowLength = 0;
		for (Point p : gamemap.getGamemap().keySet()) {
			if (p.getX() > rowLength) {
				rowLength = p.getX();
			}
		}
		return rowLength + 1;
	}

	private int getColumnLength() {
		int columnLength = 0;
		for (Point p : gamemap.getGamemap().keySet()) {
			if (p.getX() > columnLength) {
				columnLength = p.getY();
			}
		}
		return columnLength + 1;
	}

	public void addVisitedMountain(Point p) {
		visitedMountains.add(p);
	}

	public Point getClosestMountain(boolean treasureFound) {

		getPoints();

		ArrayList<Point> relevantPoints = new ArrayList<>();

		if (treasureFound) {
			relevantPoints = this.pointsInEnemyHalf;
		} else {
			relevantPoints = this.pointsInMyHalf;
		}

		Point myPosition = getPlayerPosition();
		double shortestDistance = 10000;
		Point closestMountain = null;

		for (Entry<Point, Field> entry : this.gamemap.getGamemap().entrySet()) {
			Field field = entry.getValue();
			if (field.getTerrain() == Terrain.MOUNTAIN) {
				Point key = entry.getKey();
				if (relevantPoints.contains(key)) {
					if (!this.visitedMountains.contains(key)) {
						double distance = calculateShortestDistance(key, myPosition);
						if (distance < shortestDistance) {
							shortestDistance = distance;
							closestMountain = key;
						}
					}
				}
			}
		}

		this.visitedMountains.add(closestMountain);
		return closestMountain;
	}

	private double calculateShortestDistance(Point p1, Point p2) {
		// Euclidean distance
		return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
	}

	public Point getPlayerPosition() {
		for (Entry<Point, Field> entry : this.gamemap.getGamemap().entrySet()) {
			Field field = entry.getValue();
			if (field.isPlayerOnThisField()) {
				Point key = entry.getKey();
				return key;
			}
		}
		return null;
	}

	public Point getTreasurePosition() {
		for (Entry<Point, Field> entry : this.gamemap.getGamemap().entrySet()) {
			Field field = entry.getValue();
			if (field.isTreasureOnThisField()) {
				Point key = entry.getKey();
				return key;
			}
		}
		return null;
	}

	public Point getEnemyCastlePosition() {
		for (Entry<Point, Field> entry : this.gamemap.getGamemap().entrySet()) {
			Field field = entry.getValue();
			if (field.isEnemyCastleOnThisField()) {
				Point key = entry.getKey();
				return key;
			}
		}
		return null;
	}
}
