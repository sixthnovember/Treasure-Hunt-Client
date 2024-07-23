package map;

import java.util.HashMap;

public class HalfMapGenerator {
	private HashMap<Point, Field> halfmap;
	private final int waterMinFields = 7;
	private final int mountainMinFields = 5;
	private final int grassMinFields = 24;

	public HalfMap generate() {
		while (true) {
			if (this.halfmap != null) {
				this.halfmap.clear();
			}
			this.halfmap = new HashMap<>();
			generatePoints();
			generateFields();
			HalfMapValidator validator = new HalfMapValidator(this.halfmap);
			boolean isValid = validator.check();
			if (isValid) {
				return new HalfMap(this.halfmap);
			}
		}
	}

	private void generatePoints() {
		for (int x = 0; x < Point.xMax; ++x) {
			for (int y = 0; y < Point.yMax; ++y) {
				Point p = new Point(x, y);
				this.halfmap.put(p, null);
			}
		}
	}

	private void generateFields() {
		for (Point p : this.halfmap.keySet()) {
			boolean visited = false;
			boolean enemyOnThisField = false;
			Terrain terrain = placeETerrain();
			boolean treasureOnThisField = false;
			boolean castleOnThisField = placeCastle(terrain);
			boolean playerOnThisField;
			if (castleOnThisField) {
				playerOnThisField = true;
			} else {
				playerOnThisField = false;
			}
			boolean enemyCastleOnThisField = false;
			Field field = new Field(visited, playerOnThisField, enemyOnThisField, terrain, treasureOnThisField,
					castleOnThisField, enemyCastleOnThisField);
			this.halfmap.put(p, field);
		}

	}

	private Terrain placeETerrain() {
		int grassCounter = 0;
		int mountainCounter = 0;
		int waterCounter = 0;
		Terrain terrain = Terrain.getRandomTerrain();
		if (terrain == Terrain.GRASS && grassCounter < grassMinFields) {
			grassCounter += 1;
			return Terrain.GRASS;
		} else if (terrain == Terrain.MOUNTAIN && mountainCounter < mountainMinFields) {
			mountainCounter += 1;
			return Terrain.MOUNTAIN;
		} else if (terrain == Terrain.WATER && waterCounter < waterMinFields) {
			waterCounter += 1;
			return Terrain.WATER;
		} else {
			mountainCounter += 1;
			return Terrain.MOUNTAIN;
		}
	}

	private boolean placeCastle(Terrain terrain) {
		boolean castlePlaced = false;
		if (!castlePlaced && terrain == Terrain.GRASS) {
			if (Math.random() < 0.1) {
				castlePlaced = true;
				return true;
			}
		}
		return false;
	}
}
