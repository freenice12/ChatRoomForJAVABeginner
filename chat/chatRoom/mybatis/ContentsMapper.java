package chatRoom.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ContentsMapper {
	
	@Select("SELECT id FROM client_id")
	public ArrayList<String> selectAllClientId();
	
	@Select("SELECT contents FROM room_contents WHERE clients like CONCAT('%', #{ID}, '%')")
	public ArrayList<String> searchAllContentsById(@Param("ID")String ID);
	
	@Insert("INSERT INTO client_id(id) VALUES(#{id})")
	public Integer insertNewClientId(String id);
	
	@Insert("INSERT INTO room_contents(clients, contents) VALUES(#{clients},#{contents})")
	public Integer insertContents(@Param("clients")String clients, @Param("contents")String contents);
	
}
