package fr.alex.chess.net;

public interface ClientProducer {
	public ComClient produceClient(String host, int port);
}
