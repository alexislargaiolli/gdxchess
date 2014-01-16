package fr.alex.chess.net;

public interface ComClient
{
		public void connectClient (String ip);
		
		public boolean sendMsg(String msg);

		public boolean isConnected();

		public int getId();
		
		public void close();
		
		public void setClient(ClientMSG client);
}