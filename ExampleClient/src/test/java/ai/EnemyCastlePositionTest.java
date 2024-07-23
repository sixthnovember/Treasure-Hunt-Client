package ai;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import client.main.PositionFinding;
import gamemodel.GameMap;
import map.Field;
import map.Point;
import map.Terrain;

class EnemyCastlePositionTest {

	@Test
	void createMap_searchForEnemyCastlePosition_hasFoundTheCorrectPoint() {
		HashMap<Point, Field> gamemap = new HashMap<>();

		Point p1 = new Point(0, 0);
		Field f1 = new Field(false, true, false, Terrain.GRASS, false, false, false);
		gamemap.put(p1, f1);

		Point p2 = new Point(0, 1);
		Field f2 = new Field(false, false, true, Terrain.GRASS, false, false, false);
		gamemap.put(p2, f2);

		Point p3 = new Point(1, 0);
		Field f3 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		gamemap.put(p3, f3);

		Point p4 = new Point(1, 1);
		Field f4 = new Field(false, false, false, Terrain.GRASS, false, false, true);
		gamemap.put(p4, f4);

		Point p5 = new Point(2, 0);
		Field f5 = new Field(false, false, false, Terrain.GRASS, false, true, false);
		gamemap.put(p5, f5);

		Point p6 = new Point(2, 1);
		Field f6 = new Field(false, false, false, Terrain.MOUNTAIN, false, false, false);
		gamemap.put(p6, f6);

		Point p7 = new Point(3, 0);
		Field f7 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		gamemap.put(p7, f7);

		Point p8 = new Point(3, 1);
		Field f8 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		gamemap.put(p8, f8);

		Point p9 = new Point(4, 0);
		Field f9 = new Field(false, false, false, Terrain.WATER, false, false, false);
		gamemap.put(p9, f9);

		Point p10 = new Point(4, 1);
		Field f10 = new Field(false, false, false, Terrain.GRASS, true, false, false);
		gamemap.put(p10, f10);

		GameMap createdGameMap = new GameMap(gamemap);
		PositionFinding positionFinding = new PositionFinding();
		positionFinding.setGameMap(createdGameMap);
		Point p = positionFinding.getEnemyCastlePosition();

		Assertions.assertTrue(p == p4);

	}

}
