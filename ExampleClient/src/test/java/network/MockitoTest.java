package network;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import client.main.Network;

class MockitoTest {

	@Test
	void Network_mockGetCurrentGameState_verifyIsCalledOnce() throws Exception {
		Network fakeNetwork = Mockito.mock(Network.class);
		Mockito.when(fakeNetwork.registerClient(any(), any(), any(), any()))
				.thenReturn("Player registration complete!");
		fakeNetwork.getCurrentGameState();
		Mockito.verify(fakeNetwork, times(1)).getCurrentGameState();
	}

}
