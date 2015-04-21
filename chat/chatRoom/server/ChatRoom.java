package chatRoom.server;

import java.util.ArrayList;

public class ChatRoom {
	private String title;
	private String password;
	private ArrayList<ClientInfo> clientList;
	
	public ChatRoom() {
		this.clientList = new ArrayList<>();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<ClientInfo> getClientList() {
		return clientList;
	}
	public void setClientList(ArrayList<ClientInfo> clientList) {
		this.clientList = clientList;
	}
	public boolean addClient(ClientInfo clientInfo){
		clientList.add(clientInfo);
		return true;
	}
}
