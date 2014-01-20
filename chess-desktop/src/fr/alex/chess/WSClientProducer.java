package fr.alex.chess;

import fr.alex.chess.net.ClientProducer;
import fr.alex.chess.net.ComClient;

public class WSClientProducer implements ClientProducer{

	@Override
	public ComClient produceClient(String host, int port) {		
		return new WSClient(host, port);
	}
	
}
