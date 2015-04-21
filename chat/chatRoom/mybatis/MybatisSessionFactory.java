package chatRoom.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisSessionFactory {
	private static SqlSessionFactory sqlSessionFactory;
	static {
		String resource = "mybatis_config.xml";
		try (InputStream inputStream = Resources.getResourceAsStream(resource)){
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "test");
			sqlSessionFactory.getConfiguration().addMapper(ContentsMapper.class);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static ContentsMapper getSqlSession(){
		return sqlSessionFactory.openSession().getMapper(ContentsMapper.class);
	}
		
}
