package client.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gamemodel.GameMap;
import map.Point;
import map.Terrain;

public class WayFinding {

	private GameMap gamemap;
	private final static Logger logger = LoggerFactory.getLogger(ClientManager.class);
	private final int INF = Integer.MAX_VALUE;

	public void setGameMap(GameMap gamemap) {
		this.gamemap = gamemap;
	}

	public ArrayList<EMovement> getShortestWayTo(Point myPosition, Point goal) {
		ArrayList<Point> points = dijkstraAlgorithm(myPosition, goal);
		logger.info("Points to go: " + points.stream().map(p -> String.format("(%d, %d)", p.getX(), p.getY()))
				.collect(Collectors.joining(" -> ")));
		ArrayList<EMovement> way = calculateMove(points);
		logger.info("Moves to make: "
				+ way.stream().map(w -> String.format("%s", w.name())).collect(Collectors.joining(" -> ")));
		return way;
	}

	private ArrayList<EMovement> calculateMove(ArrayList<Point> points) {
		ArrayList<EMovement> moves = new ArrayList<>();

		for (int i = 0; i < points.size() - 1; ++i) {
			Point currentPosition = points.get(i);
			Point nextPosition = points.get(i + 1);

			EMovement move;
			if (nextPosition.getX() == currentPosition.getX() + 1) {
				move = EMovement.RIGHT;
			} else if (nextPosition.getX() == currentPosition.getX() - 1) {
				move = EMovement.LEFT;
			} else if (nextPosition.getY() == currentPosition.getY() + 1) {
				move = EMovement.DOWN;
			} else {
				move = EMovement.UP;
			}

			Terrain currentTerrain = this.gamemap.getGamemap().get(currentPosition).getTerrain();
			Terrain nextTerrain = this.gamemap.getGamemap().get(nextPosition).getTerrain();
			int moveAmount = getMoveAmount(currentTerrain, nextTerrain);

			for (int j = 0; j < moveAmount; ++j) {
				moves.add(move);
			}

		}

		return moves;
	}

	private int getMoveAmount(Terrain currentTerrain, Terrain nextTearrain) {
		return Terrain.getMovesToEnterOrLeaveTerrain(currentTerrain)
				+ Terrain.getMovesToEnterOrLeaveTerrain(nextTearrain);
	}

	// TAKEN FROM START https://www.baeldung.com/cs/dfs-vs-bfs-vs-dijkstra
	// Took the pseudocode of Algorithm 8 and 9 as the foundation for my
	// impelementation
	private ArrayList<Point> dijkstraAlgorithm(Point myPosition, Point goal) {
		HashMap<Point, Integer> dist = new HashMap<>();
		HashMap<Point, Point> prev = new HashMap<>();
		Set<Point> visited = new HashSet<>();
		PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(dist::get));

		for (Point p : gamemap.getGamemap().keySet()) {
			dist.put(p, INF);
			prev.put(p, null);
		}

		dist.put(myPosition, 0);

		queue.addAll(gamemap.getGamemap().keySet());

		while (!queue.isEmpty()) {
			Point u = queue.poll();
			visited.add(u);

			ArrayList<Point> adjacentPoints = getAdjacentPoints(u);

			for (Point v : adjacentPoints) {

				if (visited.contains(v)) {
					continue;
				}

				int weightToLeave = Terrain.getMovesToEnterOrLeaveTerrain(gamemap.getGamemap().get(u).getTerrain());
				int weightToEnter = Terrain.getMovesToEnterOrLeaveTerrain(gamemap.getGamemap().get(v).getTerrain());
				int weightTotal = weightToLeave + weightToEnter;
				int vDist = dist.get(u);
				int alt = vDist + weightTotal;

				if (vDist == INF) {
					alt = INF;
				}

				if (alt < dist.get(v)) {
					dist.put(v, alt);
					prev.put(v, u);
					queue.remove(v);
					queue.add(v);
				}
			}
		}

		ArrayList<Point> path = new ArrayList<>();

		for (Point u = goal; u != null; u = prev.get(u)) {
			path.add(u);
		}

		Collections.reverse(path);

		if (path.size() > 1 && path.get(0).equals(myPosition)) {
			return path;
		}

		return new ArrayList<>();

	}
	// TAKEN FROM END https://www.baeldung.com/cs/dfs-vs-bfs-vs-dijkstra

	private ArrayList<Point> getAdjacentPoints(Point p) {
		ArrayList<Point> adjacentPoints = new ArrayList<>();
		Point right = new Point(p.getX() + 1, p.getY());
		if (isValidPoint(right)) {
			adjacentPoints.add(right);
		}
		Point down = new Point(p.getX(), p.getY() + 1);
		if (isValidPoint(down)) {
			adjacentPoints.add(down);
		}
		Point left = new Point(p.getX() - 1, p.getY());
		if (isValidPoint(left)) {
			adjacentPoints.add(left);
		}
		Point up = new Point(p.getX(), p.getY() - 1);
		if (isValidPoint(up)) {
			adjacentPoints.add(up);
		}
		return adjacentPoints;
	}

	private boolean isValidPoint(Point p) {
		if (this.gamemap.getGamemap().containsKey(p)) {
			if (this.gamemap.getGamemap().get(p).getTerrain() == Terrain.WATER) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

}
