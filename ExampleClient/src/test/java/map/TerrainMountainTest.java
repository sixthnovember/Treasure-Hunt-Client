package map;

import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TerrainMountainTest {

	@Test
	void generateHalfMap_countMountainFields_HasEnoughMountainFields() {
		HalfMapGenerator generator = new HalfMapGenerator();
		HalfMap halfmap = generator.generate();

		int mountain = 0;
		for (Entry<Point, Field> e : halfmap.getHalfmap().entrySet()) {
			if (e.getValue().getTerrain() == Terrain.GRASS) {
				++mountain;
			}
		}

		Assertions.assertTrue(mountain >= 5);
	}

}
