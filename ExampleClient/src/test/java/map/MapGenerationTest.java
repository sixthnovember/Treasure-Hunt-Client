package map;

import org.junit.jupiter.api.RepeatedTest;

public class MapGenerationTest {

	@RepeatedTest(value = 10)
	void HalfMap_createdByHalfMapGenerator_shouldBeValidEachTime() {
		HalfMapGenerator generator = new HalfMapGenerator();
		HalfMap halfmap = generator.generate();
		System.out.println(halfmap.getHalfmap().toString());
	}

}
