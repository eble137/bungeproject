package webSocket;

import java.util.Collections;


import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.websocket.*;
import javax.websocket.server.*;

import com.bunge.dao.ChatDao;

@ServerEndpoint("/broadcasting")
public class BroadSocket {
	public static Set<Session> setClients = Collections.synchronizedSet(new HashSet<Session>());
		
	@OnMessage		// 클라이언트로부터 메시지가 도착했을 때.
	public void onMessage(String message, Session session) throws Exception {
		System.out.println("from client : " + message);
		
		// message : "HSK137|||34|||말을 해요 말을"
		StringTokenizer st = new StringTokenizer(message, "|||");
		String from = st.nextToken();
		int rno = 0;
		try {
			rno = Integer.parseInt(st.nextToken());
		} catch(Exception e) { e.printStackTrace(); }
		String say = st.nextToken();
		ChatDao chatDao = new ChatDao();
		chatDao.insertChat(rno, from, say);
		
		synchronized(setClients) { // synchronized: 동기화.. block방식(이건 block방식): 스레드가 여러개인 환경에서도 잘 돌아간다 (메서드방식도 있음)
			for(Session client : setClients) { // Session: 연결, 세션하나당 하나의 연결정보를 가지고 있음
				if(!client.equals(session)) { // 목록이 나랑 같지 않다면
					client.getBasicRemote().sendText(message); // 나 이외의 클라이언트들에게 보냄
				}
			}
		}
	}

	@OnOpen			// 클라이언트가 서버로 접속했을 때.
	public void onOpen(Session session) {
		System.out.println(session);
		setClients.add(session);
		System.out.println("참여 : " + setClients.size() + "명.");
	}
	
	@OnClose		// 클라이언트의 접속이 끊어졌을 때.
	public void onClose(Session session) {
		setClients.remove(session);
	}
	
}




