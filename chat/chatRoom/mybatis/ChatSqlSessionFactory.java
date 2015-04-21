package chatRoom.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public enum ChatSqlSessionFactory {
	INSTANCE;
	
	private SqlSessionFactory sqlSessionFactory = null;
	
	private ChatSqlSessionFactory(){
		try{	
			String resource = "mybatis_config.xml";
			try (InputStream inputStream = Resources.getResourceAsStream(resource)){
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "test");
				sqlSessionFactory.getConfiguration().addMapper(ContentsMapper.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public SqlSession openSession() {
		return sqlSessionFactory.openSession();
	}
}
