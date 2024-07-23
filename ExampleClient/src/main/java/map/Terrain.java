package map;

public enum Terrain {
	GRASS, WATER, MOUNTAIN;

	public static int getMovesToEnterOrLeaveTerrain(Terrain terrain) {
		if (terrain == GRASS) {
			return 1;
		} else if (terrain == MOUNTAIN) {
			return 2;
		} else {
			return 10000;
		}
	}

	public static Terrain getRandomTerrain() {
		if (Math.random() < 0.2) {
			return Terrain.WATER;
		} else {
			if (Math.random() < 0.5) {
				return Terrain.MOUNTAIN;
			} else {
				return Terrain.GRASS;
			}
		}
	}

}
