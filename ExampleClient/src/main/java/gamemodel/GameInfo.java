package gamemodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameInfo {
	private GameMap gamemap;
	private EGameState gamestate;
	private boolean treasureFound;
	private boolean enemyCastleFound;
	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	public GameInfo() {
		this.gamestate = EGameState.WAIT;
		this.treasureFound = false;
		this.enemyCastleFound = false;
	}

	public GameMap getGamemap() {
		return gamemap;
	}

	public void setGamemap(GameMap gamemap) {
		GameMap beforeChange = this.gamemap;
		this.gamemap = gamemap;
		changes.firePropertyChange("GameMap", beforeChange, this.gamemap);
	}

	public EGameState getGamestate() {
		return gamestate;
	}

	public void setGamestate(EGameState gamestate) {
		EGameState beforeChange = this.gamestate;
		this.gamestate = gamestate;
		changes.firePropertyChange("EGameState", beforeChange, this.gamestate);
	}

	public boolean isTreasureFound() {
		return treasureFound;
	}

	public void setTreasureFound(boolean treasureFound) {
		boolean beforeChange = this.treasureFound;
		this.treasureFound = treasureFound;
		changes.firePropertyChange("treasureFound", beforeChange, this.treasureFound);
	}

	public boolean isEnemyCastleFound() {
		return enemyCastleFound;
	}

	public void setEnemyCastleFound(boolean enemyCastleFound) {
		boolean beforeChange = this.enemyCastleFound;
		this.enemyCastleFound = enemyCastleFound;
		changes.firePropertyChange("enemyCastleFound", beforeChange, this.enemyCastleFound);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

}
