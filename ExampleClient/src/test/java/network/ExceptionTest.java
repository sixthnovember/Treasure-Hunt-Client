package network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import client.main.Network;
import exception.PlayerRegistrationException;

class ExceptionTest {

	@Test
	void gameIDIsInvalid_registerClient_throwsException() {
		String randomInvalidGameID = "12345";
		Network network = new Network("http://swe1.wst.univie.ac.at:18235", randomInvalidGameID);
		Executable testCode = () -> network.registerClient(randomInvalidGameID, "Nicole", "Gladik", "01639509");
		Assertions.assertThrows(PlayerRegistrationException.class, testCode,
				"We expected a exception because because server and gameid are null.");
	}

}
