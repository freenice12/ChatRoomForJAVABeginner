package chatRoom.model;

import java.util.ArrayList;

public class ClientContents {
	private ArrayList<String> clients;
	private String contents;
	public ClientContents(){
		clients = new ArrayList<String>();
	}
	public ArrayList<String> getClients() {
		return clients;
	}
	public void setClients(ArrayList<String> clients) {
		this.clients = clients;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
