package map;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.main.ClientManager;

public class HalfMapValidator {

	private HashMap<Point, Field> halfmap;
	private final int yLimit = 2;
	private final int xLimit = 4;
	private final int castleCounterLimit = 1;
	private final int waterMinFields = 7;
	private final int mountainMinFields = 5;
	private final int grassMinFields = 24;
	private final int halfMapSize = 50;
	private final static Logger logger = LoggerFactory.getLogger(ClientManager.class);

	public HalfMapValidator(HashMap<Point, Field> halfmap) {
		this.halfmap = halfmap;
	}

	public boolean check() {
		return checkIfPlayerIsOnCastle() && checkSize() && checkIfCastleOnGrass() && checkGrassPercent()
				&& checkMountainPercent() && checkWaterPercent() && checkIfCastleExists()
				&& checkIfOnlyOneCastleExists() && checkIfNoIsland() && checkIfOtherHalfIsReachable();
	}

	private boolean checkIfOtherHalfIsReachable() {

		Field[][] halfmap = new Field[Point.xMax][Point.yMax];

		for (Entry<Point, Field> entrySet : this.halfmap.entrySet()) {
			halfmap[entrySet.getKey().getX()][entrySet.getKey().getY()] = entrySet.getValue();
		}

		int waterMinX = 0;
		int waterMinY = 0;
		int waterMaxX = 0;
		int waterMaxY = 0;

		for (int x = Point.xMin; x < Point.xMax; ++x) {
			for (int y = Point.yMin; y < Point.yMax; ++y) {
				if (halfmap[x][y].getTerrain() == Terrain.WATER) {
					if (x == Point.xMin) {
						waterMinX += 1;
					}

					if (x == Point.xMax) {
						waterMaxX += 1;
					}

					if (y == Point.yMin) {
						waterMinY += 1;
					}

					if (y == Point.yMax) {
						waterMaxY += 1;
					}
				}
			}
		}

		if (waterMinX > this.xLimit || waterMaxX > this.xLimit || waterMinY > this.yLimit || waterMaxY > this.yLimit) {
			logger.warn(
					"checkIfOtherHalfIsReachable() failed! waterMinX: {}, waterMaxX: {}, waterMinY: {}, waterMaxY: {}",
					waterMinX, waterMaxX, waterMinY, waterMaxY);
			return false;
		}

		return true;

	}

	private boolean checkIfNoIsland() {

		Field[][] halfmapToCheck = new Field[Point.xMax][Point.yMax];

		for (Entry<Point, Field> entrySet : this.halfmap.entrySet()) {
			halfmapToCheck[entrySet.getKey().getX()][entrySet.getKey().getY()] = entrySet.getValue();
		}

		floodFillAlgorithm(halfmapToCheck, 0, 0, true);

		for (int x = Point.xMin; x < Point.xMax; ++x) {
			for (int y = Point.yMin; y < Point.yMax; ++y) {
				if (halfmapToCheck[x][y].getTerrain() != Terrain.WATER && !halfmapToCheck[x][y].isVisited()) {
					logger.warn("checkIfNoIsland() failed! The following field could not be visited: ({}, {})", x, y);
					return false;
				}
			}
		}

		floodFillAlgorithm(halfmapToCheck, 0, 0, false);

		return true;
	}

	// TAKEN FROM START
	// https://www.sanfoundry.com/java-program-flood-fill-algorithm/
	// took the idea of the implementation of the flood fill algorithm
	private void floodFillAlgorithm(Field[][] halfmap, int x, int y, boolean value) {

		if (x < Point.xMin || x >= Point.xMax || y < Point.yMin || y >= Point.yMax) {
			return;
		}

		if (halfmap[x][y].getTerrain() == Terrain.WATER) {
			return;
		}

		if (halfmap[x][y].isVisited()) {
			return;
		}

		halfmap[x][y].setVisited(value);

		floodFillAlgorithm(halfmap, x + 1, y, value);
		floodFillAlgorithm(halfmap, x - 1, y, value);
		floodFillAlgorithm(halfmap, x, y + 1, value);
		floodFillAlgorithm(halfmap, x, y - 1, value);

	}
	// TAKEN FROM END https://www.sanfoundry.com/java-program-flood-fill-algorithm/

	private boolean checkIfOnlyOneCastleExists() {
		int castle_counter = 0;
		for (Field field : this.halfmap.values()) {
			if (field.isCastleOnThisField()) {
				castle_counter += 1;
			}
		}
		if (castle_counter > castleCounterLimit) {
			logger.warn("checkIfOnlyOneCastleExists() failed! There are multiple castles: " + castle_counter);
			return false;
		}
		return true;
	}

	private boolean checkIfCastleExists() {
		for (Field field : this.halfmap.values()) {
			if (field.isCastleOnThisField()) {
				return true;
			}
		}
		logger.warn("checkIfCastleExists() failed! Castle does not exist!");
		return false;
	}

	private boolean checkWaterPercent() {
		int waterCounter = 0;
		for (Field field : this.halfmap.values()) {
			if (field.getTerrain() == Terrain.WATER) {
				waterCounter += 1;
			}
		}
		if (waterCounter >= waterMinFields) {
			return true;
		}
		logger.warn("checkWaterPercent() failed! There are only {} of at least 7 water fields!", waterCounter);
		return false;
	}

	private boolean checkMountainPercent() {
		int mountainCounter = 0;
		for (Field field : this.halfmap.values()) {
			if (field.getTerrain() == Terrain.MOUNTAIN) {
				mountainCounter += 1;
			}
		}
		if (mountainCounter >= mountainMinFields) {
			return true;
		}
		logger.warn("checkMountainPercent() failed! There are only {} of at least 5 mountain fields!", mountainCounter);
		return false;
	}

	private boolean checkGrassPercent() {
		int grassCounter = 0;
		for (Field field : this.halfmap.values()) {
			if (field.getTerrain() == Terrain.GRASS) {
				grassCounter += 1;
			}
		}
		if (grassCounter >= grassMinFields) {
			return true;
		}
		logger.warn("checkGrassPercent() failed! There are only {} of at least 24 grass fields!", grassCounter);
		return false;
	}

	private boolean checkIfCastleOnGrass() {
		for (Field field : this.halfmap.values()) {
			if (field.isCastleOnThisField()) {
				if (field.getTerrain() == Terrain.GRASS) {
					return true;
				}
			}
		}
		logger.warn("checkIfCastleOnGrass() failed! Castle is not on a grass field!");
		return false;
	}

	private boolean checkSize() {
		if (this.halfmap.size() == this.halfMapSize) {
			return true;
		}
		logger.warn("checkSize() failed! Size is not correct! HalfMap size: {}", this.halfmap.size());
		return false;
	}

	private boolean checkIfPlayerIsOnCastle() {
		for (Field field : this.halfmap.values()) {
			if (field.isCastleOnThisField()) {
				if (field.isPlayerOnThisField()) {
					return true;
				}
			}
		}
		logger.warn("checkIfPlayerIsOnCastle() failed! Player is not on castle position!");
		return false;
	}

}
