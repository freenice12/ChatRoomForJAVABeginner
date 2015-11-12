package chatRoom.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import chatRoom.model.DataModel;
import chatRoom.model.ProtocolEnum;

public class ClientChat {
	private InputStream inputStream;
	private ObjectInputStream objectInputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	protected ChatRoomListFrame chatRoomListFrame;
	private ChatRoomFrame chatRoomFrame;
	private String clientId;
	private String chatRoomTitle;
	private String chatRoomPassword;
	private Logger logger = Logger.getLogger(this.getClass());

	public String getChatRoomPassword() {
		return chatRoomPassword;
	}
	
	public String getChatRoomTitle(){
		return chatRoomTitle;
	}

	public static void main(String[] args) {
		new ClientChat().chatStart();
	}

	private void chatStart() {
		try {
			ChatConnector chatConnector = new ChatConnector();
			outputStream = chatConnector.getOutputStream();
			inputStream = chatConnector.getInputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectInputStream = new ObjectInputStream(inputStream);
			

			registeClientId();
			requestRoomTitles();

			while (true) {

				DataModel dataModel = (DataModel) objectInputStream.readObject();
				execute(dataModel);
			}
		} catch (ClassNotFoundException e4) {
			e4.printStackTrace();
		} catch (abnomalTerminationException e2) {
			logger.info("Abnomal Terminated");
		} catch (SocketException e1) {
			logger.info(clientId + " Quit");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registeClientId() throws abnomalTerminationException {
		getClientId();
		if (clientId == null) {
			throw new abnomalTerminationException();
		}
	}

	private void getClientId() {
		clientId = JOptionPane.showInputDialog(null, "Enter Id:",
				"Input your ID", JOptionPane.DEFAULT_OPTION);
	}

	public void requestRoomTitles() {
		DataModel data = new DataModel(ProtocolEnum.REQUESTTITLES);
		data.setId(clientId);
		sendData(data);

	}


	public void sendData(Object data) {
		try {
			objectOutputStream.writeObject(data);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void execute(DataModel completeDataModel) {
		ProtocolEnum protocolEnum = completeDataModel.getProtocolEnum();
		switch (protocolEnum) {
		case ALLTITLE:
			buildChatRoomListFrame(completeDataModel);
			break;
		case CLIENTSLIST:
			buildChatRoomFrame(completeDataModel);
			break;
		case MESSAGETOALLCLIENT:
			printMessage(completeDataModel);
			logger.debug("receiveMessage : " + completeDataModel.getMessage());
			break;
		case WHISPERTOCLIENT:
			showWhisperMessage(completeDataModel);
			break;
		case ENTERAGAIN:
			enterAgain(completeDataModel);
			break;
		default:
			break;
		}

	}

	private void enterAgain(DataModel completeDataModel) {
		ArrayList<String> allTitle = completeDataModel.getPickedDataList();
		chatRoomListFrame = new ChatRoomListFrame(this, allTitle, clientId);
	}

	public void buildChatRoomListFrame(DataModel completeDataModel) {
		ArrayList<String> allTitle = completeDataModel.getPickedDataList();
		if(chatRoomListFrame == null){
			chatRoomListFrame = new ChatRoomListFrame(this, allTitle, clientId);
		} else {
			chatRoomListFrame.updateTitleList(allTitle);
		}
	}

	private void buildChatRoomFrame(DataModel completeDataModel) {
		ArrayList<String> clients = completeDataModel.getPickedDataList();
		String log = completeDataModel.getMessage();
		String[] clientArray = convertClientArrayListToArray(clients);
		if (chatRoomFrame == null) {
			clientId = clientArray[clientArray.length - 1];
			chatRoomFrame = new ChatRoomFrame(this, clientArray);
		}
		chatRoomFrame.updateClientList(clientArray);
		if(log != null && log.length() > 0 && clientId.equals(completeDataModel.getId())){
			chatRoomFrame.updateChatTextArea(log);
		}
	}

	private String[] convertClientArrayListToArray(ArrayList<String> clients) {
		String[] clientArray = new String[clients.size()];
		int count = 0;
		for (String client : clients) {
			clientArray[count] = client;
			count++;
		}
		return clientArray;
	}

	private void printMessage(DataModel completeDataModel) {
		String transmitterId = completeDataModel.getId();
		String message = completeDataModel.getMessage();
		chatRoomFrame.updateChatTextArea(transmitterId, message);
	}

	private void showWhisperMessage(DataModel completeDataModel) {
		String transmitterId = completeDataModel.getTransmitterId();
		String whisperMessage = completeDataModel.getMessage();
		chatRoomFrame.updateChatTextAreaToShowWhisper(transmitterId,
				whisperMessage);
	}

	public void enterRoom(String title, String password) {
		setMyInfo(title, password);
		DataModel data = new DataModel(ProtocolEnum.ENTERROOM);
		data.setTitle(title);
		data.setPassword(password);
		data.setId(clientId);

		sendData(data);
	}


	public void createRoom(String title, String password) {
		setMyInfo(title, password);

		DataModel data = new DataModel(ProtocolEnum.CREATEROOM);
		data.setTitle(title);
		data.setPassword(password);
		data.setId(clientId);
		
		sendData(data);
	}


	private void setMyInfo(String title, String password) {
		chatRoomTitle = title;
		chatRoomPassword = password;
	}

	public void quitRoom(String log) {
		String title = chatRoomTitle;
		
		DataModel data = new DataModel(ProtocolEnum.QUITROOM);
		data.setTitle(title);
		data.setId(clientId);
		data.setMessage(log);

		sendData(data);

	}


	public void sendMessage(String message) {
		String title = chatRoomTitle;

		StringTokenizer messageTokenizer = new StringTokenizer(message, " ");
		int numberOfTokens = messageTokenizer.countTokens();
		if (messageTokenizer.nextToken().equals("/w") && numberOfTokens > 2) {
			sendWhisperMessage(title, messageTokenizer);
			return;
		}

		DataModel data = new DataModel(ProtocolEnum.MESSAGETOALLCLIENT);
		data.setTitle(title);
		data.setId(clientId);
		data.setMessage(message);
		
		sendData(data);
	}

	private void sendWhisperMessage(String title,
			StringTokenizer messageTokenizer) {
		String receiverId = messageTokenizer.nextToken();
		while (!chatRoomFrame.clientIdListModel.contains(receiverId)) {
			receiverId += " "+messageTokenizer.nextToken();
		}
		String whisperMessage = messageTokenizer.nextToken("\n").trim();
		
		DataModel data = new DataModel(ProtocolEnum.WHISPERTOCLIENT);
		data.setTitle(title);
		data.setId(receiverId);
		data.setTransmitterId(clientId);
		data.setMessage(whisperMessage);

		chatRoomFrame.updateChatTextAreaToReflect(receiverId, whisperMessage);
		sendData(data);
	}

}
