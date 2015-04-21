package chatRoom.server;

import java.io.ObjectOutputStream;

public class ClientInfo {
	private String id;
	private ObjectOutputStream outputStream;
	private String title;
	
	public ClientInfo() {}
	public ClientInfo(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
}
