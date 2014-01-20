package fr.alex.chess.net;



public class ClientMSG
{
	ComClient cc;
	
	//For Bidirectional Communication mode
	public ClientMSG(ComClient client)
	{
		cc = client;
		cc.setClient(this);
	}


	public void onMessage(String message)
	{
		System.out.println("ClientMSG msg received: " + message);
		//String [] values = message.split("\\s+"); //splitter with the " " separator
		
		//int ClientID = Integer.valueOf(values[0]); //Check of the ID (not required)
		
		//Calls to the upper level class methods
		Net.onMessage(message);
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