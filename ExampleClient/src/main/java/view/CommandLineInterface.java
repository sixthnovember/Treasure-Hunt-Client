package view;

import java.beans.PropertyChangeListener;
import java.util.HashMap;

import gamemodel.EGameState;
import gamemodel.GameInfo;
import map.Field;
import map.Point;

public class CommandLineInterface {

	public CommandLineInterface(GameInfo model) {
		model.addPropertyChangeListener(modelChangedListener);
	}

	final PropertyChangeListener modelChangedListener = event -> {
		Object model = event.getSource();
		if (model instanceof GameInfo) {
			GameInfo castedModel = (GameInfo) model;
			printGameMap(castedModel.getGamemap().getGamemap());
			printTreasureFound(castedModel.isTreasureFound());
			printEnemyCastleFound(castedModel.isEnemyCastleFound());
			printGameState(castedModel.getGamestate());
		}
	};

	private void printGameMap(HashMap<Point, Field> gamemap) {
		for (int y = 0; y < Point.yMax * 2; ++y) {
			for (int x = 0; x < Point.xMax * 2; x++) {
				Point point = new Point(x, y);
				Field field = gamemap.get(point);
				if (field != null) {
					System.out.print(field);
				}
			}
			System.out.println();
		}
	}

	private void printTreasureFound(boolean treasureFound) {
		System.out.println("Treasure found: " + treasureFound);
	}

	private void printEnemyCastleFound(boolean enemyCastleFound) {
		System.out.println("Enemy castle found: " + enemyCastleFound);
	}

	private void printGameState(EGameState gamestate) {
		if (gamestate == EGameState.WON || gamestate == EGameState.LOST) {
			System.out.println("GameState: " + gamestate.toString());
		} else {
			System.out.println("GameState: PLAYING");
		}
	}

}
