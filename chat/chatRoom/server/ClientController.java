package chatRoom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import chatRoom.model.ClientContents;
import chatRoom.model.DataModel;
import chatRoom.model.ProtocolEnum;
import chatRoom.mybatis.ContentsDao;

public class ClientController implements Runnable {
	private Socket clientSocket;
	private InputStream inputStream;
	private ObjectInputStream objectInputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	private ClientInfo clientInfo;
	private ArrayList<ChatRoom> chatRoomList;
	private ArrayList<ClientInfo> totalClientList;
	private final Map<String, ClientContents> contentsManager; 
	
//	private MybatisConnector mybatisConnector;	
	private ContentsDao contentsDao;

	private Logger logger = Logger.getLogger(ClientController.class);

	public ClientController(ServerChat serverChat) {
		chatRoomList = serverChat.getChatManager().getChatRoomList();
		totalClientList = serverChat.getChatManager().getTotalClientList();
		contentsManager = serverChat.getContentsManager();
		clientSocket = serverChat.getClientSocket();
		try {
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectInputStream = new ObjectInputStream(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		mybatisConnector = new MybatisConnector();
		contentsDao = new ContentsDao();

		addClient();
	}

	private void addClient() {
		this.clientInfo = new ClientInfo(objectOutputStream);
		this.clientInfo.setId(clientSocket.getInetAddress().toString());
		this.totalClientList.add(clientInfo);
	}

	@Override
	public void run() {
		try {
			while (true) {

				DataModel dataModel = (DataModel) objectInputStream
						.readObject();
				execute(dataModel);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (TimeoutException e){
			e.printStackTrace();
		} catch (SocketException e) {
			terminationProcess();
			if (clientInfo.getId() != null) {
				logger.info("client left(Sock) : " + clientInfo.getId());
			}
		} catch (IOException e) {
			terminationProcess();
			if (clientInfo.getId() != null) {
				logger.info("client left(IOE) : " + clientInfo.getId());
			}
		} catch (Exception e){
			logger.debug("DB Exception");
			logger.error(e);
		}
	}

	public void execute(DataModel dataModel) throws Exception {
		ProtocolEnum protocolEnum = dataModel.getProtocolEnum();
		switch (protocolEnum) {
		case MESSAGETOALLCLIENT:
			sendMessageToChatRoom(dataModel);
			break;
		case WHISPERTOCLIENT:
			whisperToClient(dataModel);
			break;
		case CREATEROOM:
			createRoom(dataModel);
			logger.debug("create : " + dataModel.getTitle() + " / id : "
					+ dataModel.getId());
			enterChatRoom(dataModel);
			break;
		case ENTERROOM:
			if (enterChatRoom(dataModel)){
				logger.debug("enter : (title) " + dataModel.getTitle());
			} else {
				logger.info("S : SER : wrong password -> tryAgain()");
				tryAgain();
			}
			break;
		case QUITROOM:
			findClientAndRemove(dataModel);
			break;
		case REQUESTTITLES:
			modifyClientId(dataModel.getId());
			sendAllTitle();
			break;
		default:
			break;
		}

	}

	private void sendMessageToChatRoom(DataModel dataModel) throws IOException {
		String title = dataModel.getTitle();
		String id = dataModel.getId();
		String message = dataModel.getMessage();

		ChatRoom chatRoom = findChatRoom(title);

		sendMassage(chatRoom, id, message);

	}

	public ChatRoom findChatRoom(String title) {
		for (ChatRoom chatRoom : chatRoomList) {
			if (chatRoom.getTitle().equals(title)) {
				return chatRoom;
			}
		}
		return null;
	}

	private void sendMassage(ChatRoom chatRoom, String id, String message)
			throws IOException {
		DataModel data = new DataModel(ProtocolEnum.MESSAGETOALLCLIENT);
		data.setTitle(chatRoom.getTitle());
		data.setId(id);
		data.setMessage(message);
		
		
		
		
		ClientContents clientContents = contentsManager.get(chatRoom.getTitle());
		clientContents.setContents(clientContents.getContents()+id+" : "+message+"\n");
		
		
		

		Iterator<ClientInfo> chatClientIter = chatRoom.getClientList()
				.iterator();

		int count = 0;

		while (chatClientIter.hasNext()) {
			ClientInfo client = chatClientIter.next();
			@SuppressWarnings("resource")
			ObjectOutputStream clientOutputStream = client.getOutputStream();
			sendData(clientOutputStream, data);
			count++;
		}
		logger.debug("S : SMA(" + count + ") : " + data.getMessage());
	}

	private void sendData(ObjectOutputStream outputStreamToClient,
			DataModel data) throws IOException {
		outputStreamToClient.writeObject(data);
		outputStreamToClient.flush();
	}

	private ClientInfo findClientByIdFromChatRoom(ChatRoom room, String id) {
		Iterator<ClientInfo> chatClientIter = room.getClientList().iterator();
		while (chatClientIter.hasNext()) {
			ClientInfo client = chatClientIter.next();
			if (client.getId().equals(id)) {
				return client;
			}
		}
		return null;
	}

	private void whisperToClient(DataModel dataModel) throws IOException {
		String title = dataModel.getTitle();
		String receiverId = dataModel.getId();
		String whisperMessage = dataModel.getMessage();
		String transmitterId = dataModel.getTransmitterId();

		ChatRoom chatRoom = findChatRoom(title);
		ClientInfo receiver = findClientByIdFromChatRoom(chatRoom, receiverId);

		@SuppressWarnings("resource")
		ObjectOutputStream outputStreamToReceiver = receiver.getOutputStream();

		DataModel data = new DataModel(ProtocolEnum.WHISPERTOCLIENT);
		data.setTitle(title);
		data.setId(receiverId);
		data.setMessage(whisperMessage);
		data.setTransmitterId(transmitterId);
		
		ClientContents clientContents = contentsManager.get(chatRoom.getTitle());
		clientContents.setContents(clientContents.getContents()+"From("+transmitterId+") -> To("+receiverId+") : "+whisperMessage+"\n");

		logger.debug("S : SWT : (from)" + data.getId() + " (to) "
				+ data.getTransmitterId() + " (message) " + data.getMessage());
		sendData(outputStreamToReceiver, data);
	}

	private void createRoom(DataModel dataModel) throws IOException {
		String title = dataModel.getTitle();
		String password = dataModel.getPassword();
		String clientId = dataModel.getId();
		if (!isNew(title)) {
			logger.info("S : SCR : Title already exists");
			tryAgain();
			return;
		}
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setTitle(title);
		chatRoom.setPassword(password);

		ClientInfo client = findClientById(clientId);

		chatRoom.addClient(client);

		chatRoomList.add(chatRoom);
		
		
		
		
		ClientContents clientContents = new ClientContents();
		clientContents.getClients().add(clientId);
//		clientContents.setClients(clientId+" ");
//		clientContents.setContents("\t["+clientId+"]입장 ("+title+" / "+password+")\n");
		clientContents.setContents("\t["+clientId+"]입장\n");
		contentsManager.put(title, clientContents);
		
	}

	private boolean isNew(String title) {
		for (ChatRoom chat : chatRoomList) {
			if (chat.getTitle().equals(title)) {
				return false;
			}
		}
		return true;
	}

	private ClientInfo findClientById(String clientId) {
		for (ClientInfo client : totalClientList) {
			if (client.getId().equals(clientId)) {
				return client;
			}
		}
		return null;
	}

	private boolean enterChatRoom(DataModel completeDataModel) throws IOException {
		String title = completeDataModel.getTitle();
		clientInfo.setTitle(title);
		String password = completeDataModel.getPassword();
		String clientId = clientInfo.getId();

		ChatRoom chatRoom = findChatRoom(title);
		if (chatRoom.getPassword().equals(password)) {
			ClientInfo client = findClientById(clientId);
			if (completeDataModel.getProtocolEnum() == ProtocolEnum.ENTERROOM) {
				chatRoom.addClient(client);
				ClientContents clientContents = contentsManager.get(title);
				clientContents.getClients().add(clientId);
//				clientContents.setClients(clientContents.getClients()+clientId+" ");
				clientContents.setContents(clientContents.getContents()+"\n\t["+clientId+"]입장\n");
			}
			DataModel clientsListDataModel = makeClientList(chatRoom);
			clientsListDataModel.setId(clientId);
			
			
			try{
				ArrayList<String> allContents = contentsDao.searchAllContentsById(clientId);
				StringBuffer contents = new StringBuffer();
				for(String content : allContents){
					String extractedContent = contentsDao.extractContents(clientId, content);
					contents.append(extractedContent);
				}
				clientsListDataModel.setMessage(contents.toString());
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			
			
			
			sendDataClientList(chatRoom, clientsListDataModel);
			logger.debug("S : SCL : (client List)"
					+ clientsListDataModel.getPickedDataList().toString());
			return true;
		}
		return false;
	}

	private DataModel makeClientList(ChatRoom chatRoom) {
		DataModel idsDataModel = getClientsIdFromChatRoom(chatRoom);
		return idsDataModel;
	}

	private DataModel getClientsIdFromChatRoom(ChatRoom chatRoom) {
		DataModel clientsListDataModel = new DataModel(ProtocolEnum.CLIENTSLIST);
//		Iterator<ClientInfo> chatClientListIter = chatRoom.getClientList()
//				.iterator();
//		while (chatClientListIter.hasNext()) {
//			String id = chatClientListIter.next().getId();
//			clientsListDataModel.getPickedDataList().add(id);
//		}
		
		for(ClientInfo clientInfoa:chatRoom.getClientList()){
			String id = clientInfoa.getId();
			clientsListDataModel.getPickedDataList().add(id);
		}
		
		return clientsListDataModel;
	}

	private void sendDataClientList(ChatRoom chatRoom, DataModel dataClientList)
			throws IOException {
		Iterator<ClientInfo> chatClientListIter = chatRoom.getClientList()
				.iterator();
		while (chatClientListIter.hasNext()) {
			ClientInfo pClient = chatClientListIter.next();
			@SuppressWarnings("resource")
			ObjectOutputStream clientOutputStream = pClient.getOutputStream();
			sendData(clientOutputStream, dataClientList);
		}
	}

	private void tryAgain() throws IOException {
		DataModel data = new DataModel(ProtocolEnum.ENTERAGAIN);
		if (chatRoomList.size() > 0) {
			for (ChatRoom room : chatRoomList) {
				String title = room.getTitle();
				data.getPickedDataList().add(title);
			}
		}

		logger.debug("S : SEA : (title List)"
				+ data.getPickedDataList().toString());

		sendData(objectOutputStream, data);
	}

	private void findClientAndRemove(DataModel dataModel) throws IOException {
		String title = dataModel.getTitle();
		String clientId = dataModel.getId();
//		String log = dataModel.getMessage();

		ChatRoom chatRoom = findChatRoom(title);
		removeClient(clientId, chatRoom);

		int numberOfClient = chatRoom.getClientList().size();
		if (numberOfClient < 1) {
			
			ClientContents clientContents = contentsManager.get(title);
			clientContents.setContents(clientContents.getContents()+"\t["+clientId+"]퇴장\n");
			try {
				contentsDao.insertContents(clientContents);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			
			chatRoomList.remove(chatRoom);
			logger.info("remove Room : " + chatRoom.getTitle());
		}
		
//		ClientContents clientContents = new ClientContents();
//		clientContents.setId(id);
//		clientContents.setLog(log);
//
//		if(mybatisConnector.selectLogById(id) != null){
//			mybatisConnector.updateClientLog(clientContents);
//			return;
//		}
//		mybatisConnector.insertClientLog(clientContents);

	}

	private void removeClient(String id, ChatRoom chatRoom) throws IOException {
		ClientInfo client = findClientById(id);
		chatRoom.getClientList().remove(client);
		if (client != null) {
			totalClientList.remove(client);
		}
		if (chatRoom.getClientList().isEmpty() != true) {
			DataModel dataClientList = makeClientList(chatRoom);
			sendDataClientList(chatRoom, dataClientList);
			logger.debug("S : SCL(QR) : "
					+ dataClientList.getPickedDataList().toString());
		}
	}

	public void modifyClientId(String clientId) {
		logger.info("[" + clientId + "("+clientSocket.getInetAddress().toString()+")] has connected");
		clientInfo.setId(clientId);
		totalClientList.add(clientInfo);
		try{
			List<String> allClientId = contentsDao.selectAllClientId();
			for (String existId : allClientId) {
				if (existId != null && clientId.equals(existId)) {
					return;
				}
			}
			contentsDao.insertNewClientId(clientId);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		
//		mybatisConnector = new MybatisConnector();
//		List<String> allClientId = mybatisConnector.selectAllClientId();
//		for (String existId : allClientId) {
//			if (existId != null && clientId.equals(existId)) {
//				return;
//			}
//		}
//		mybatisConnector.insertNewClientId(clientId);
	}

	private void sendAllTitle() throws IOException {
		DataModel data = new DataModel(ProtocolEnum.ALLTITLE);
		if (chatRoomList.size() > 0) {
			for (ChatRoom room : chatRoomList) {
				String title = room.getTitle();
				data.getPickedDataList().add(title);
			}
		}

		logger.debug("S : SAT : (title List)"
				+ data.getPickedDataList().toString());

		sendData(objectOutputStream, data);
	}

	private void terminationProcess() {
		if (clientInfo.getId() == null) {
			logger.info("Client : termination");
			return;
		}
		
		
		ClientInfo quitClient = findClientById(clientInfo.getId());
		if (quitClient != null) {
			totalClientList.remove(quitClient);
			if(quitClient.getTitle() != null){
				ClientContents clientContents = contentsManager.get(quitClient.getTitle());
				clientContents.setContents(clientContents.getContents()+"\t["+quitClient.getId()+"]퇴장\n");
			}
		}

	}

}