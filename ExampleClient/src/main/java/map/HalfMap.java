package map;

import java.util.HashMap;

public class HalfMap {

	private HashMap<Point, Field> halfmap;

	public HalfMap(HashMap<Point, Field> halfmap) {
		super();
		this.halfmap = halfmap;
	}

	public HashMap<Point, Field> getHalfmap() {
		return this.halfmap;
	}

}
