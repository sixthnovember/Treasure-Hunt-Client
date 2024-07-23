package client.main;

import gamemodel.GameInfo;
import view.CommandLineInterface;

public class MainClient {

	public static void main(String[] args) {

		String serverBaseUrl = args[1];
		String gameId = args[2];

		GameInfo model = new GameInfo();
		Network network = new Network(serverBaseUrl, gameId);
		CommandLineInterface cli = new CommandLineInterface(model);
		ClientManager clientmanager = new ClientManager(model, cli, network);

		clientmanager.registerClient(gameId, "Nicole", "Gladik", "nicoleg97");
		clientmanager.playGame();
	}
}
