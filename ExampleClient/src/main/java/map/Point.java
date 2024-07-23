package map;

import java.util.Comparator;
import java.util.Objects;

public class Point {

	private int x;
	private int y;

	public static final int xMax = 10;
	public static final int yMax = 5;
	public static final int xMin = 0;
	public static final int yMin = 0;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public boolean isValid(Point point) {
		if (point.getX() >= Point.xMin && point.getY() >= Point.yMin && point.getX() <= Point.xMax
				&& point.getY() <= Point.yMax) {
			return true;
		}
		return false;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}

	public static Comparator<Point> pointComparator = new Comparator<Point>() {
		@Override
		public int compare(Point p1, Point p2) {
			if (p1.x != p2.x) {
				return Integer.compare(p1.x, p2.x);
			} else {
				return Integer.compare(p1.y, p2.y);
			}
		}
	};

}
