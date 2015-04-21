package chatRoom.server;

import java.util.ArrayList;

public class ChatManager {
	private ArrayList<ChatRoom> chatRoomList;
	private ArrayList<ClientInfo> totalClientList;
	
	public ChatManager() {
		chatRoomList = new ArrayList<>();
		totalClientList = new ArrayList<>();
	}
	public ArrayList<ChatRoom> getChatRoomList() {
		return chatRoomList;
	}
	public ArrayList<ClientInfo> getTotalClientList() {
		return totalClientList;
	}
	
	
}
