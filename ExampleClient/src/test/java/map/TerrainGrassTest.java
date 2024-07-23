package map;

import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TerrainGrassTest {

	@Test
	void generateHalfMap_countGrassFields_HasEnoughGrassFields() {
		HalfMapGenerator generator = new HalfMapGenerator();
		HalfMap halfmap = generator.generate();

		int grass = 0;
		for (Entry<Point, Field> e : halfmap.getHalfmap().entrySet()) {
			if (e.getValue().getTerrain() == Terrain.GRASS) {
				++grass;
			}
		}

		Assertions.assertTrue(grass >= 24);
	}

}
