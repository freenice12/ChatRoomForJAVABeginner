package chatRoom.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import chatRoom.model.ClientContents;

public class ContentsDao {
	private Logger logger = Logger.getLogger(ContentsDao.class);
	private final int TIMEOUT_PER_SEC = 1;
	ExecutorService executor = Executors.newSingleThreadExecutor();
	
	
	public List<String> selectAllClientId() throws Exception{
		final SqlSession session = ChatSqlSessionFactory.INSTANCE.openSession();
		try{
			
			final ContentsMapper mapper = session.getMapper(ContentsMapper.class);
			List<String> clientList = new ArrayList<>();
			
			Future<List<String>> future = executor.submit(new Callable<List<String>>(){
				@Override
				public List<String> call() throws Exception {					
					List<String> selectAllClientId = mapper.selectAllClientId();
//					Thread.sleep(2000);
//					if(selectAllClientId == null)
//						return Collections.emptyList();
					
				    return selectAllClientId;
				}
				
			});
			
			clientList = future.get(TIMEOUT_PER_SEC, TimeUnit.SECONDS); 
			
			logger.debug(clientList.toString());
			return clientList;
		} catch (TimeoutException e){
			throw new TimeoutException("selectAllClientId Timeout!");
		} catch (Exception e){
			throw new Exception("selectAllClientId Exception");
		} finally {
			session.close();
		}
	}
	
	public ArrayList<String> searchAllContentsById(final String ID) throws Exception{
		 
		final SqlSession session = ChatSqlSessionFactory.INSTANCE.openSession();
		try{
			final ContentsMapper mapper = session.getMapper(ContentsMapper.class);
			ArrayList<String> clientContents = new ArrayList<String>(); 
			
			Future<ArrayList<String>> future = executor.submit(new Callable<ArrayList<String>>(){
				@Override
				public ArrayList<String> call() throws Exception {		
					ArrayList<String> clientContent = mapper.searchAllContentsById(ID);
					
				    return clientContent;
				}
				
			});
			
			clientContents = future.get(TIMEOUT_PER_SEC, TimeUnit.SECONDS); 
			logger.info(ID+"has connected");
			return clientContents;
		}catch (TimeoutException e){
			throw new TimeoutException("searchAllContents Timeout!");
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception("searchAllContents Exception");
		}finally {
			session.close();
		}
	}

	public String extractContents(String clientId, String clientContents) {
		StringBuffer contentsBuffer = new StringBuffer();
		Pattern enter = Pattern.compile("\\["+clientId+"\\]입장");
		Pattern left = Pattern.compile("\\["+clientId+"\\]퇴장");
		Matcher enterMatcher = enter.matcher(clientContents);
		Matcher leftMatcher = left.matcher(clientContents);
		
		if(enterMatcher.find() && leftMatcher.find()){
			int enterStart = enterMatcher.start();
			int leftEnd = leftMatcher.end();
			while(leftMatcher.find(leftEnd)){
				leftEnd = leftMatcher.end();
			}
			contentsBuffer.append(clientContents.substring(enterStart-1,leftEnd+1));
		}
		logger.info("successful extraction");
		return contentsBuffer.toString();
	}

	public void insertNewClientId(final String clientId) throws Exception{
		final SqlSession session = ChatSqlSessionFactory.INSTANCE.openSession();
		try{
			final ContentsMapper mapper = session.getMapper(ContentsMapper.class);
			@SuppressWarnings("boxing")
			Integer result = 0;
			
			Future<Integer> future = executor.submit(new Callable<Integer>(){
				@Override
				public Integer call() throws Exception {					
					Integer resultState = mapper.insertNewClientId(clientId);
					
//					if(selectAllClientId == null)
//						return Collections.emptyList();
					
				    return resultState;
				}
				
			});
			result = future.get(TIMEOUT_PER_SEC, TimeUnit.SECONDS); 			
			commitSQL(session, result);
			logger.info("Registe New Client("+clientId+")");
		}catch (TimeoutException e){
			throw new TimeoutException("insertNewClientId Timeout!");
		}catch (Exception e){
			throw new Exception("insertNewClientId Exception");
		}finally{
			session.close();
		}
	}

	@SuppressWarnings("boxing")
	private void commitSQL(SqlSession session, Integer result) {
		if(result > 0){
			session.commit();
		}
	}

	public void insertContents(final ClientContents clientContents) throws Exception{
		final SqlSession session = ChatSqlSessionFactory.INSTANCE.openSession();
		try{
			final ContentsMapper mapper = session.getMapper(ContentsMapper.class);
			@SuppressWarnings("boxing")
			Integer result = 0;
			List<String> clients = clientContents.getClients();
			final String clientsId = clients.toString();
			Future<Integer> future = executor.submit(new Callable<Integer>(){
				@Override
				public Integer call() throws Exception {					
					Integer resultState = mapper.insertContents(clientsId, clientContents.getContents());
					
//					if(selectAllClientId == null)
//						return Collections.emptyList();
					
				    return resultState;
				}
				
			});
			
			result = future.get(TIMEOUT_PER_SEC, TimeUnit.SECONDS);
			commitSQL(session, result);
			logger.info("success to inserting contents");
		}catch (TimeoutException e){
			throw new TimeoutException("insertContents Timeout!");
		}catch (Exception e){
			throw new Exception("insertContents Exception");
		}finally{
			session.close();
		}
	}
}
