package fr.alex.chess;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import fr.alex.chess.net.ClientMSG;
import fr.alex.chess.net.ComClient;

//This is the Standard WebSocket Implementation

public class WSClient implements ComClient 
{	
	private int port;
	private WebSocketClient wsc; //Websocket
	private boolean connected;
	private ClientMSG client;
	
	//For Bidirectional Communication mode
	public WSClient (String ip, int port)
	{
		this.port = port;
		connected = false;
	}
	
	public void connectClient (String ip)
	{
		if (!ip.isEmpty())
		{
			//Websocket implementation
			URI url = null; //URI (url address of the server)
			try {
				url = new URI("ws://"+ ip +":"+ port); //We create the URI of the server. Use a port upper than 1024 on Android and Linux!
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 

			//We select the standard implementation of WebSocket
			Draft standard = new Draft_17(); 

			wsc = new WebSocketClient( url, standard) {

				@Override
				public void onOpen( ServerHandshake handshake ) {
					connected = true;
				}
				
				@Override
				public void onMessage( String message ) {
					client.onMessage(message);
				}

				@Override
				public void onError( Exception ex ) {
					ex.printStackTrace();
					System.out.println("WSClient Error.");
				}
				
				@Override
				public void onClose( int code, String reason, boolean remote ) {
					connected = false;
					System.out.println("WSClient closed. reaseon: " + reason + ". code: "+code);
				}
			};
			wsc.connect(); //And we create the connection between client and server
			
		}
	}

	public boolean sendMsg(String msg)
	{
		if (connected)
		{
			System.out.println("WSClient send: " + msg);
			wsc.send(msg);
			//wsc.onMessage(arg0)
			return true;
		}
		else return false;
	}

	public boolean isConnected()
	{
		return connected;
	}
	
	public void close()
	{
		wsc.close();
		connected = false;
	}

	public ClientMSG getClient() {
		return client;
	}

	public void setClient(ClientMSG client) {
		this.client = client;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
