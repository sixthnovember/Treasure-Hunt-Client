package map;

import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TerrainWaterTest {

	@Test
	void generateHalfMap_countWaterFields_HasEnoughWaterFields() {
		HalfMapGenerator generator = new HalfMapGenerator();
		HalfMap halfmap = generator.generate();

		int water = 0;
		for (Entry<Point, Field> e : halfmap.getHalfmap().entrySet()) {
			if (e.getValue().getTerrain() == Terrain.GRASS) {
				++water;
			}
		}

		Assertions.assertTrue(water >= 7);
	}

}
