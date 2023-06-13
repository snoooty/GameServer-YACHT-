package yachtDiceGameServer2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

public class createServer {
	
	static ArrayList<Socket> users = new ArrayList<Socket>();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		// 포트번호
		int port = 9000;
		
		// 서버소켓을 생성하고 포트번호 설정
		ServerSocket serverSocket = new ServerSocket(port);
		RoomManager rManager = new RoomManager();
		YachtDiceRoom ydRoom = new YachtDiceRoom();
		
		
		while(true) {
			
			Socket sock = serverSocket.accept();
			UserInfo user = new UserInfo(sock);
			
			System.out.println("유저 소켓" + sock);			
			System.out.println("사용자가 접속했습니다.");
			System.out.println(sock.getInetAddress()+"의 "+sock.getPort()
			+ "포트에" + sock.getLocalAddress()+"의 "+sock.getLocalPort()
			+ "포트로 연결되었습니다.");
			System.out.println("접속시간 : " + LocalTime.now());
			
			users.add(sock);
			
			user.enterRoom(ydRoom);// 유저 방에입장
			ydRoom.enterUser(user);// 방에 유저 정보 설정
			
			if(rManager.roomCount() == 0) {// 최초로 방을 방 리스트에 추가
				ydRoom.setRoomNum(sock.getPort() + sock.getLocalPort());// 방번호 설정
				System.out.println("방 번호 설정 : " + ydRoom.getRoomNum());
				rManager.createRoom(ydRoom);// 방생성
				System.out.println("방 생성 후 roomCount : " + rManager.roomCount());
			}else if(rManager.roomList.get(rManager.roomCount() -1).GetUserSize() == 2
					&& users.size() > rManager.roomCount() * 2){// 최근 방에 유저 수가 최대일때 마다 방 리스트에 추가
				ydRoom = new YachtDiceRoom();
				user.enterRoom(ydRoom);// 유저 방에입장
				ydRoom.enterUser(user);// 방에 유저 정보 설정
				ydRoom.setRoomNum(sock.getPort() + sock.getLocalPort());// 방번호 설정
				System.out.println("방 번호 설정 : " + ydRoom.getRoomNum());
				rManager.createRoom(ydRoom);// 방생성
				System.out.println("방 생성 후 roomCount : " + rManager.roomCount());
			}
			
			System.out.println("현재 방 번호 : " + ydRoom.getRoomNum());
			System.out.println("들어간 유저 정보 : " + ydRoom.userList.get(ydRoom.GetUserSize() - 1).getSock());
			System.out.println("방 생성 후 userCount : " + ydRoom.GetUserSize());
									
			for(int i = 0; i < rManager.roomCount(); i++) {
				System.out.println(rManager.roomList.get(i).getRoomNum() + "번방의 유저 수 : "
			+ rManager.roomList.get(i).GetUserSize());
			}
			
			
			ActionThread acThread = new ActionThread(sock, users, rManager);
		}
		
	}

}
