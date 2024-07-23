package map;

import java.util.Objects;

public class Field {

	private boolean visited;
	private boolean playerOnThisField;
	private boolean enemyOnThisField;
	private Terrain terrain;
	private boolean treasureOnThisField;
	private boolean castleOnThisField;
	private boolean enemyCastleOnThisField;

	// TAKEN FROM START
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	// took the color codes
	private final String ANSI_RESET = "\u001B[0m";
	private final String ANSI_BLACK = "\u001B[30m";
	private final String ANSI_RED = "\u001B[31m";
	private final String ANSI_GREEN = "\u001B[32m";
	private final String ANSI_YELLOW = "\u001B[33m";
	private final String ANSI_BLUE = "\u001B[34m";
	private final String ANSI_PURPLE = "\u001B[35m";
	private final String ANSI_CYAN = "\u001B[36m";
	private final String ANSI_WHITE = "\u001B[37m";
	// TAKEN FROM END
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public Field(boolean visited, boolean playerOnThisField, boolean enemyOnThisField, Terrain terrain,
			boolean treasureOnThisField, boolean castleOnThisField, boolean enemyCastleOnThisField) {
		super();
		this.visited = visited;
		this.playerOnThisField = playerOnThisField;
		this.enemyOnThisField = enemyOnThisField;
		this.terrain = terrain;
		this.treasureOnThisField = treasureOnThisField;
		this.castleOnThisField = castleOnThisField;
		this.enemyCastleOnThisField = enemyCastleOnThisField;
	}

	public boolean isVisited() {
		return this.visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isPlayerOnThisField() {
		return this.playerOnThisField;
	}

	public void setPlayerOnThisField(boolean playerOnThisField) {
		this.playerOnThisField = playerOnThisField;
	}

	public boolean isEnemyOnThisField() {
		return this.enemyOnThisField;
	}

	public void setEnemyOnThisField(boolean enemyOnThisField) {
		this.enemyOnThisField = enemyOnThisField;
	}

	public Terrain getTerrain() {
		return this.terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public boolean isTreasureOnThisField() {
		return this.treasureOnThisField;
	}

	public void setTreasureOnThisField(boolean treasureOnThisField) {
		this.treasureOnThisField = treasureOnThisField;
	}

	public boolean isCastleOnThisField() {
		return this.castleOnThisField;
	}

	public void setCastleOnThisField(boolean castleOnThisField) {
		this.castleOnThisField = castleOnThisField;
	}

	public boolean isEnemyCastleOnThisField() {
		return enemyCastleOnThisField;
	}

	public void setEnemyCastleOnThisField(boolean enemyCastleOnThisField) {
		this.enemyCastleOnThisField = enemyCastleOnThisField;
	}

	@Override
	public int hashCode() {
		return Objects.hash(castleOnThisField, enemyCastleOnThisField, enemyOnThisField, playerOnThisField, terrain,
				treasureOnThisField, visited);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		return castleOnThisField == other.castleOnThisField && enemyCastleOnThisField == other.enemyCastleOnThisField
				&& enemyOnThisField == other.enemyOnThisField && playerOnThisField == other.playerOnThisField
				&& terrain == other.terrain && treasureOnThisField == other.treasureOnThisField
				&& visited == other.visited;
	}

	@Override
	public String toString() {
		if (this.playerOnThisField && this.enemyOnThisField) {
			return ANSI_RED + "\u2694" + ANSI_RESET; // sword emoji
		} else if (this.playerOnThisField) {
			return ANSI_CYAN + "\uD83D\uDC69" + ANSI_RESET; // woman emoji
		} else if (this.enemyOnThisField) {
			return ANSI_PURPLE + "\uD83D\uDE08" + ANSI_RESET; // devil emoji
		} else if (this.treasureOnThisField) {
			return ANSI_YELLOW + "\uD83D\uDCB0" + ANSI_RESET; // money bag emoji
		} else if (this.castleOnThisField) {
			return ANSI_BLACK + "\uD83C\uDFE0" + ANSI_RESET; // house emoji
		} else if (this.enemyCastleOnThisField) {
			return ANSI_BLACK + "\uD83C\uDFF0" + ANSI_RESET; // castle emoji
		} else {
			if (this.terrain == Terrain.GRASS) {
				return ANSI_GREEN + "\uD83C\uDF43" + ANSI_RESET; // leaf emoji
			} else if (this.terrain == Terrain.MOUNTAIN) {
				return ANSI_WHITE + "\uD83C\uDFD4" + ANSI_RESET; // mountain emoji
			} else {
				return ANSI_BLUE + "\uD83C\uDF0A" + ANSI_RESET; // water wave emoji
			}
		}
	}
}
