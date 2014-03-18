package fr.alex.chess.client;

import fr.alex.chess.net.ComClient;
import net.zschech.gwt.websockets.client.CloseHandler;
import net.zschech.gwt.websockets.client.ErrorHandler;
import net.zschech.gwt.websockets.client.MessageEvent;
import net.zschech.gwt.websockets.client.MessageHandler;
import net.zschech.gwt.websockets.client.OpenHandler;
import net.zschech.gwt.websockets.client.WebSocket;

public class GWTClient implements ComClient {
	// private static int DEFAULT_SERVER_PORT = 80;
	private int port;
	private WebSocket wsc; // Websocket to connect to a remote server
	private boolean connected;
	private fr.alex.chess.net.ClientMSG c;

	// For Bidirectional Communication mode
	public GWTClient(String ip, int port) {
		this.port = port;
		this.connected = false;
		this.connectClient(ip);
	}

	public void connectClient(String ip) {

		if (!ip.isEmpty()) {
			// We create the URI in String format
			String url = null; // URI (url address of the server)
			url = new String("ws://" + ip + ":" + port);

			try {
				wsc = WebSocket.create(url); // "ws://echo.websocket.org" //For
												// the echo testing server

				// Handler methods override the original methods for the
				// webSocket functionality
				wsc.setOnOpen(new OpenHandler() {
					@Override
					public void onOpen(WebSocket webSocket) {
						connected = true;
					}
				});

				wsc.setOnMessage(new MessageHandler() {
					@Override
					public void onMessage(WebSocket webSocket,
							MessageEvent event) {
						String message = event.getData();
						c.onMessage(message);
					}
				});

				wsc.setOnError(new ErrorHandler() {
					@Override
					public void onError(WebSocket webSocket) {
						System.out.println("GWTClient Error.");
					}
				});

				wsc.setOnClose(new CloseHandler() {
					@Override
					public void onClose(WebSocket webSocket) {
						connected = false;
					}
				});
			} catch (Exception e) {
			}
		}
	}

	public boolean sendMsg(String msg) {
		if (connected) {
			wsc.send(msg);
			return true;
		} else
			return false;
	}

	public boolean isConnected() {
		return connected;
	}

	public void close() {
		wsc.close();
		connected = false;
	}

	@Override
	public void setClient(fr.alex.chess.net.ClientMSG client) {
		this.c = client;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}