package converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import map.Field;
import map.HalfMap;
import map.Point;
import map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

class HalfMapConverterTest {

	@Test
	void createHalfMapAndPlayerHalfMap_convertHalfMap_equalsPlayerHalfMap() {
		HashMap<Point, Field> halfmap = new HashMap<>();
		Collection<PlayerHalfMapNode> playerHalfMapNodes = new ArrayList<>();

		Point p1 = new Point(0, 0);
		Field f1 = new Field(false, true, false, Terrain.GRASS, false, false, false);
		halfmap.put(p1, f1);
		PlayerHalfMapNode n1 = new PlayerHalfMapNode(0, 0, false, ETerrain.Grass);
		playerHalfMapNodes.add(n1);

		Point p2 = new Point(0, 1);
		Field f2 = new Field(false, false, true, Terrain.GRASS, false, false, false);
		halfmap.put(p2, f2);
		PlayerHalfMapNode n2 = new PlayerHalfMapNode(0, 1, false, ETerrain.Grass);
		playerHalfMapNodes.add(n2);

		Point p3 = new Point(1, 0);
		Field f3 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		halfmap.put(p3, f3);
		PlayerHalfMapNode n3 = new PlayerHalfMapNode(1, 0, false, ETerrain.Grass);
		playerHalfMapNodes.add(n3);

		Point p4 = new Point(1, 1);
		Field f4 = new Field(false, false, false, Terrain.GRASS, false, false, true);
		halfmap.put(p4, f4);
		PlayerHalfMapNode n4 = new PlayerHalfMapNode(1, 1, false, ETerrain.Grass);
		playerHalfMapNodes.add(n4);

		Point p5 = new Point(2, 0);
		Field f5 = new Field(false, false, false, Terrain.GRASS, false, true, false);
		halfmap.put(p5, f5);
		PlayerHalfMapNode n5 = new PlayerHalfMapNode(2, 0, true, ETerrain.Grass);
		playerHalfMapNodes.add(n5);

		Point p6 = new Point(2, 1);
		Field f6 = new Field(false, false, false, Terrain.MOUNTAIN, false, false, false);
		halfmap.put(p6, f6);
		PlayerHalfMapNode n6 = new PlayerHalfMapNode(2, 1, false, ETerrain.Mountain);
		playerHalfMapNodes.add(n6);

		Point p7 = new Point(3, 0);
		Field f7 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		halfmap.put(p7, f7);
		PlayerHalfMapNode n7 = new PlayerHalfMapNode(3, 0, false, ETerrain.Grass);
		playerHalfMapNodes.add(n7);

		Point p8 = new Point(3, 1);
		Field f8 = new Field(false, false, false, Terrain.GRASS, false, false, false);
		halfmap.put(p8, f8);
		PlayerHalfMapNode n8 = new PlayerHalfMapNode(3, 1, false, ETerrain.Grass);
		playerHalfMapNodes.add(n8);

		Point p9 = new Point(4, 0);
		Field f9 = new Field(false, false, false, Terrain.WATER, false, false, false);
		halfmap.put(p9, f9);
		PlayerHalfMapNode n9 = new PlayerHalfMapNode(4, 0, false, ETerrain.Water);
		playerHalfMapNodes.add(n9);

		Point p10 = new Point(4, 1);
		Field f10 = new Field(false, false, false, Terrain.GRASS, true, false, false);
		halfmap.put(p10, f10);
		PlayerHalfMapNode n10 = new PlayerHalfMapNode(4, 1, false, ETerrain.Grass);
		playerHalfMapNodes.add(n10);

		HalfMap halfmapToConvert = new HalfMap(halfmap);
		HalfMapToPlayerHalfMapConverter converter = new HalfMapToPlayerHalfMapConverter(halfmapToConvert, "12345");
		PlayerHalfMap convertedPlayerHalfMap = converter.convert();
		PlayerHalfMap createdPlayerHalfMap = new PlayerHalfMap("12345", playerHalfMapNodes);

		Assertions.assertEquals(convertedPlayerHalfMap, createdPlayerHalfMap);
	}

}
