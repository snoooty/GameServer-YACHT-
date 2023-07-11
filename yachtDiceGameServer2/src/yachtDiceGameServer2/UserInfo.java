package yachtDiceGameServer2;

import java.net.Socket;

public class UserInfo {
	
	Socket sock = null;
	YachtDiceRoom ydRoom;
	String user_name;
	int roomNum;
	
	public UserInfo(Socket sock) {
		this.sock = sock;
	}
	
	public Socket getSock() {
		return sock;
	}
	
	public String getUser_name() {
		return user_name;
	}
	
	public void setUser_name(String name) {
		this.user_name = name;
	}
	
	public int getRoomNum() {
		return roomNum;
	}
	
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	
	public void enterRoom(YachtDiceRoom room) {
		this.ydRoom = room;
	}
}
