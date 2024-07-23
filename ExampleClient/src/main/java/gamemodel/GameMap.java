package gamemodel;

import java.util.HashMap;

import map.Field;
import map.Point;

public class GameMap {

	private HashMap<Point, Field> gamemap;

	public GameMap(HashMap<Point, Field> gamemap) {
		super();
		this.gamemap = gamemap;
	}

	public HashMap<Point, Field> getGamemap() {
		return gamemap;
	}

	public void setGamemap(HashMap<Point, Field> gamemap) {
		this.gamemap = gamemap;
	}

}
