package chatRoom.model;

public enum ProtocolEnum {
	CREATEROOM("CREATEROOM"),
	ENTERROOM("ENTERROOM"),
	MESSAGETOALLCLIENT("MESSAGETOALLCLIENT"),
	WHISPERTOCLIENT("WHISPERTOCLIENT"),
	QUITROOM("QUITROOM"),
	ALLTITLE("ALLTITLE"),
	REQUESTTITLES("REQUESTTITLES"),
	CLIENTSLIST("CLIENTSLIST"),
	FORLOG("FORLOG"),
	ENTERAGAIN("ENTERAGAIN");
	
	String value;
	
	ProtocolEnum(String value){
		this.value = value;
	}
	
	public static ProtocolEnum orderStringToEnum(String orderStr) {
		for(ProtocolEnum enumValue : ProtocolEnum.values()){
			if(enumValue.value.equals(orderStr))
				return enumValue;
		}
		
		return null;
	}
	
}
