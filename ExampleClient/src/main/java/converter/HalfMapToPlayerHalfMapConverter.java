package converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import map.Field;
import map.HalfMap;
import map.Point;
import map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

public class HalfMapToPlayerHalfMapConverter implements IConverter<PlayerHalfMap> {

	private HalfMap halfmap;
	private String uniquePlayerID;

	public HalfMapToPlayerHalfMapConverter(HalfMap halfmap, String uniquePlayerID) {
		super();
		this.halfmap = halfmap;
		this.uniquePlayerID = uniquePlayerID;
	}

	@Override
	public PlayerHalfMap convert() {
		HashMap<Point, Field> halfmapToConvert = this.halfmap.getHalfmap();
		Collection<PlayerHalfMapNode> playerHalfMapNodes = new ArrayList<>();
		for (Point p : halfmapToConvert.keySet()) {
			PlayerHalfMapNode node = convertToPlayerHalfMapNode(p, halfmapToConvert.get(p));
			playerHalfMapNodes.add(node);
		}
		return new PlayerHalfMap(this.uniquePlayerID, playerHalfMapNodes);
	}

	private PlayerHalfMapNode convertToPlayerHalfMapNode(Point point, Field field) {
		int x = point.getX();
		int y = point.getY();
		boolean fortPresent = field.isCastleOnThisField();
		ETerrain terrain = convertToETerrain(field.getTerrain());
		return new PlayerHalfMapNode(x, y, fortPresent, terrain);
	}

	private ETerrain convertToETerrain(Terrain terrain) {
		if (terrain == Terrain.GRASS) {
			return ETerrain.Grass;
		} else if (terrain == Terrain.MOUNTAIN) {
			return ETerrain.Mountain;
		} else {
			return ETerrain.Water;
		}
	}

}
