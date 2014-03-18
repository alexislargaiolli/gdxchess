package fr.alex.chess.net;

import fr.alex.chess.ChessGame;



public class ClientMSG
{
	ComClient cc;
	Net net;
	ChessGame game;
	
	//For Bidirectional Communication mode
	public ClientMSG(ComClient client, ChessGame game)
	{
		cc = client;
		cc.setClient(this);
		this.net = game.network;
		this.game = game;
	}

	public void connect(){
		cc.connectClient(this.game.gameServiceUrl);
	}
	
	public void onMessage(String message)
	{
		System.out.println("ClientMSG msg received: " + message);
		net.onMessage(message);
	}


	public boolean sendMessage(String message)
	{		
		if (cc != null && cc.isConnected())
		{
			//Only send message
			return (cc.sendMsg(message));
		}	
		else return false; 
	}
	
	public int getId()
	{
		return (cc.getId());
	}
	
	//get name from client class
	
	//one method for each messages / actions that the client can do
	
	public void close()
	{
		cc.close();
	}
	
	public boolean isConnected(){
		return cc != null && cc.isConnected();
	}
}