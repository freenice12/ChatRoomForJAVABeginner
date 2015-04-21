package chatRoom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import chatRoom.model.ClientContents;

public class ServerChat {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ChatManager chatManager;
	private final Map<String, ClientContents> contentsManager; 
	

	public ServerChat() {
		chatManager = new ChatManager();
		contentsManager = new ConcurrentHashMap<String, ClientContents>();
	}
	public Map<String, ClientContents> getContentsManager(){
		return contentsManager;
	}
	public Socket getClientSocket() {
		return clientSocket;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}

	private Logger logger = Logger.getLogger(this.getClass());

	public static void main(String[] args) {
		new ServerChat().serverRun();
	}

	private void serverRun() {
		try {
			serverSocket = new ServerSocket(5000);
			logger.info("Server Running...");
			while (true) {
				logger.info("Waiting for connection");
				clientSocket = serverSocket.accept();
				logger.info("got a connection");

				Thread eachClientThread = new Thread(new ClientController(this));
				eachClientThread.start();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
