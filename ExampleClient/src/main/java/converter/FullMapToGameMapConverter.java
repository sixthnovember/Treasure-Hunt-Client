package converter;

import java.util.Collection;
import java.util.HashMap;

import gamemodel.GameMap;
import map.Field;
import map.Point;
import map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

public class FullMapToGameMapConverter implements IConverter<GameMap> {

	private FullMap fullmap;

	public FullMapToGameMapConverter(FullMap fullmap) {
		super();
		this.fullmap = fullmap;
	}

	@Override
	public GameMap convert() {
		Collection<FullMapNode> nodes = this.fullmap.getMapNodes();
		HashMap<Point, Field> gamemap = new HashMap<>();
		for (FullMapNode f : nodes) {
			Field field = convertToField(f.getTerrain(), f.getFortState(), f.getPlayerPositionState(),
					f.getTreasureState());
			Point point = convertToPoint(f.getX(), f.getY());
			gamemap.put(point, field);
		}
		return new GameMap(gamemap);
	}

	private Field convertToField(ETerrain eterrain, EFortState fortState, EPlayerPositionState playerPositionState,
			ETreasureState treasureState) {
		boolean visited = false;
		boolean playerOnThisField = convertToPlayerOnThisField(playerPositionState);
		boolean enemyOnThisField = convertToEnemyOnThisField(playerPositionState);
		Terrain terrain = convertToTerrain(eterrain);
		boolean treasureOnThisField = convertToTreasureOnThisField(treasureState);
		boolean castleOnThisField = convertToCastleOnThisField(fortState);
		boolean enemyCastleOnThisField = convertToEnemyCastleOnThisField(fortState);
		return new Field(visited, playerOnThisField, enemyOnThisField, terrain, treasureOnThisField, castleOnThisField,
				enemyCastleOnThisField);
	}

	private boolean convertToPlayerOnThisField(EPlayerPositionState playerPositionState) {
		if (playerPositionState == EPlayerPositionState.MyPlayerPosition
				|| playerPositionState == EPlayerPositionState.BothPlayerPosition) {
			return true;
		}
		return false;
	}

	private boolean convertToEnemyOnThisField(EPlayerPositionState playerPositionState) {
		if (playerPositionState == EPlayerPositionState.EnemyPlayerPosition
				|| playerPositionState == EPlayerPositionState.BothPlayerPosition) {
			return true;
		}
		return false;
	}

	private Terrain convertToTerrain(ETerrain eterrain) {
		if (eterrain == ETerrain.Grass) {
			return Terrain.GRASS;
		} else if (eterrain == ETerrain.Mountain) {
			return Terrain.MOUNTAIN;
		} else {
			return Terrain.WATER;
		}
	}

	private boolean convertToTreasureOnThisField(ETreasureState treasureState) {
		if (treasureState == ETreasureState.MyTreasureIsPresent) {
			return true;
		}
		return false;
	}

	private boolean convertToCastleOnThisField(EFortState fortState) {
		if (fortState == EFortState.MyFortPresent) {
			return true;
		}
		return false;
	}

	private boolean convertToEnemyCastleOnThisField(EFortState fortState) {
		if (fortState == EFortState.EnemyFortPresent) {
			return true;
		}
		return false;
	}

	private Point convertToPoint(int x, int y) {
		return new Point(x, y);
	}

}
