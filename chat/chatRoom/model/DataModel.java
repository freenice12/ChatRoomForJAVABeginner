package chatRoom.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DataModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String password;
	private String message;
	private String transmitterId;
	private ArrayList<String> pickedDataList;
	private ProtocolEnum protocolEnum;

	public DataModel() {
		pickedDataList = new ArrayList<>();
	}


	public DataModel(ProtocolEnum protocol) {
		this();
		orderStringToEnum(protocol);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTransmitterId(String transmitterId) {
		this.transmitterId = transmitterId;
	}

	public ArrayList<String> getPickedDataList() {
		return pickedDataList;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getPassword() {
		return password;
	}

	public String getTransmitterId() {
		return transmitterId;
	}

	public String getMessage() {
		return message;
	}

	public ProtocolEnum getProtocolEnum() {
		return protocolEnum;
	}

	private void orderStringToEnum(ProtocolEnum protocol) {

		protocolEnum = protocol;

	}

}
