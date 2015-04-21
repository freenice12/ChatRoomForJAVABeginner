package chatRoom.model;

public class DBController {
//	private Connection connection;
//	private PreparedStatement preparedStatement;
//	private ResultSet resultSet;
//	
//	
//	
//	public DBController() {
//		connection = DBConnector.getDBConnection();
//	}
//	
//	private void init(){
//		preparedStatement = null;
//		resultSet = null;
//	}
//	
//	public ArrayList<String> getAllClientId() throws SQLException{
//		init();
//		ArrayList<String> result = new ArrayList<>();
//		String sql = "select id from client_id";
//		connection.createStatement();
//		preparedStatement = connection.prepareStatement(sql);
//		resultSet = preparedStatement.executeQuery();
//		while(resultSet.next()){
//			String clientID = resultSet.getString(1);
//			result.add(clientID);
//		}
//		return result;
//	}
//
//	public void insertNewClientId(String newClientId) throws SQLException {
//		init();
//		String sql = "insert into client_id(id) values(?)";
//		preparedStatement = connection.prepareStatement(sql);
//		preparedStatement.setString(1, newClientId);
//		preparedStatement.execute();
//	}
//	
//	public boolean searchClientLog(String clientId) throws SQLException {
//		init();
//		String sql = "select * from chat_message where id = ?";
//		preparedStatement = connection.prepareStatement(sql);
//		preparedStatement.setString(1, clientId);
//		resultSet = preparedStatement.executeQuery();
//		return resultSet.next();
//	}
//
//	public void insertClientLog(String clientId, String logMessage) throws SQLException {
//		init();
//		String sql = "insert into chat_message(id, message) values(?,?)";
//		preparedStatement = connection.prepareStatement(sql);
//		preparedStatement.setString(1, clientId);
//		preparedStatement.setString(2, logMessage);
//		preparedStatement.execute();
//	}
//
//	public void updateClientLog(String clientId, String logMessage) throws SQLException {
//		init();
//		String sql = "update chat_message set message=? where id=?";
//		preparedStatement = connection.prepareStatement(sql);
//		preparedStatement.setString(1, logMessage);
//		preparedStatement.setString(2, clientId);
//		preparedStatement.execute();
//	}
//	
//	public ArrayList<String> getClientLog(String clientId) throws SQLException {
//		init();
//		ArrayList<String> clientLogArray = new ArrayList<>();
//		String sql = "select message from chat_message where id=?";
//		preparedStatement = connection.prepareStatement(sql);
//		preparedStatement.setString(1, clientId);
//		resultSet = preparedStatement.executeQuery();
//		while(resultSet.next()){
//			String logMessage = resultSet.getString(1);
//			clientLogArray.add(logMessage);
//		}
//		return clientLogArray;
//	}


}
