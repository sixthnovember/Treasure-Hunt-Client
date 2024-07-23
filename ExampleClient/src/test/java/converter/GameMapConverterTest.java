package converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gamemodel.GameMap;
import map.Field;
import map.Point;
import map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

class GameMapConverterTest {

	@Test
	void createGameMapAndFullMap_convertFullMapToGameMap_equalsGameMap() {
		HashMap<Point, Field> gamemap = new HashMap<>();
		Collection<FullMapNode> fullMapNodes = new ArrayList<>();

		Point p1 = new Point(0, 0);
		Field f1 = new Field(false, true, false, Terrain.GRASS, false, false, false);
		gamemap.put(p1, f1);
		FullMapNode n1 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.MyPlayerPosition,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 0, 0);
		fullMapNodes.add(n1);

		Point p2 = new Point(0, 1);
		Field f2 = new Field(false, false, true, Terrain.GRASS, false, false, false);
		gamemap.put(p2, f2);
		FullMapNode n2 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.EnemyPlayerPosition,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 0, 1);
		fullMapNodes.add(n2);

		Point p3 = new Point(1, 0);
		Field f3 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		gamemap.put(p3, f3);
		FullMapNode n3 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 1, 0);
		fullMapNodes.add(n3);

		Point p4 = new Point(1, 1);
		Field f4 = new Field(false, false, false, Terrain.GRASS, false, false, true);
		gamemap.put(p4, f4);
		FullMapNode n4 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.EnemyFortPresent, 1, 1);
		fullMapNodes.add(n4);

		Point p5 = new Point(2, 0);
		Field f5 = new Field(false, false, false, Terrain.GRASS, false, true, false);
		gamemap.put(p5, f5);
		FullMapNode n5 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, 2, 0);
		fullMapNodes.add(n5);

		Point p6 = new Point(2, 1);
		Field f6 = new Field(false, false, false, Terrain.MOUNTAIN, false, false, false);
		gamemap.put(p6, f6);
		FullMapNode n6 = new FullMapNode(ETerrain.Mountain, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 2, 1);
		fullMapNodes.add(n6);

		Point p7 = new Point(3, 0);
		Field f7 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		gamemap.put(p7, f7);
		FullMapNode n7 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 3, 0);
		fullMapNodes.add(n7);

		Point p8 = new Point(3, 1);
		Field f8 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		gamemap.put(p8, f8);
		FullMapNode n8 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 3, 1);
		fullMapNodes.add(n8);

		Point p9 = new Point(4, 0);
		Field f9 = new Field(false, false, false, Terrain.WATER, false, false, false);
		gamemap.put(p9, f9);
		FullMapNode n9 = new FullMapNode(ETerrain.Water, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 4, 0);
		fullMapNodes.add(n9);

		Point p10 = new Point(4, 1);
		Field f10 = new Field(false, false, false, Terrain.GRASS, true, false, false);
		gamemap.put(p10, f10);
		FullMapNode n10 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.MyTreasureIsPresent, EFortState.NoOrUnknownFortState, 4, 1);
		fullMapNodes.add(n10);

		GameMap createdGameMap = new GameMap(gamemap);
		FullMap fullMapToConvert = new FullMap(fullMapNodes);
		FullMapToGameMapConverter converter = new FullMapToGameMapConverter(fullMapToConvert);
		GameMap convertedGameMap = converter.convert();

		Assertions.assertEquals(createdGameMap.getGamemap(), convertedGameMap.getGamemap());
	}

}
