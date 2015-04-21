package chatRoom.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatConnector {
	private Socket clientSocket;
	private OutputStream outputStream;
	private InputStream inputStream;
	public ChatConnector() {
		String ip = "127.0.0.1";
		int port = 5000;
		
		try {
			clientSocket = new Socket(ip, port);
			outputStream = clientSocket.getOutputStream();
			inputStream = clientSocket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	
}
