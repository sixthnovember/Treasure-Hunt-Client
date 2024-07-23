package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import gamemodel.GameInfo;
import gamemodel.GameMap;
import map.Field;
import map.HalfMap;
import map.HalfMapGenerator;
import map.Point;

class ViewTest {

	@Test
	void HalfMap_setToGameMap_getsPrinted() {

		HalfMapGenerator generator = new HalfMapGenerator();
		HalfMap halfmap = generator.generate();
		GameMap gamemap = new GameMap(null);
		gamemap.setGamemap(halfmap.getHalfmap());
		GameInfo model = new GameInfo();
		@SuppressWarnings("unused")
		CommandLineInterface view = new CommandLineInterface(model);
		model.setGamemap(gamemap);

		List<Point> sortedKeys = new ArrayList<>(gamemap.getGamemap().keySet());
		Collections.sort(sortedKeys, Point.pointComparator);
		Map<Point, Field> sortedMap = new LinkedHashMap<>();

		for (Point key : sortedKeys) {
			sortedMap.put(key, gamemap.getGamemap().get(key));
		}

		for (Entry<Point, Field> e : sortedMap.entrySet()) {
			System.out.println(
					"(" + e.getKey().getX() + "," + e.getKey().getY() + ") -> " + e.getValue().getTerrain().toString());
		}

	}

}
