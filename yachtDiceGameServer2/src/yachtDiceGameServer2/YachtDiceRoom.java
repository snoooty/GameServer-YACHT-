package yachtDiceGameServer2;

import java.util.ArrayList;
import java.util.List;

public class YachtDiceRoom {
	
List<UserInfo> userList;
	
	String roomName;// 방이름
	int roomNum;
	
	//아무도 없는방
	public YachtDiceRoom() {// 빈 방
		userList = new ArrayList<UserInfo>();
	}

	public YachtDiceRoom(UserInfo user) {// 방생성
		userList = new ArrayList<UserInfo>();
		userList.add(user);
	}
	
	public void enterUser(UserInfo user) {// 유저 입장		
		if(GetUserSize() == 2) {
			System.out.println("방이 전부 찼습니다.");
		}else {
			userList.add(user);
			System.out.println("유저입장");
		}
	}
	
	public void exitRoom(UserInfo userSock) {// 유저 퇴장
		userList.remove(userSock);
		
		if(userList.size() < 1) {// 유저가 전부 나가면 방삭제
//			RoomManager.removeRoom(this);
			return;
		}
	}
	
	public void setRoomName(String roomName) {// 방이름설정
		this.roomName = roomName + "의 방";
	}
	
	public String GetRoomName() {// 방이름 가져오기
		return roomName;
	}
	
	public int getRoomNum() {
		return roomNum;
	}
	
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	
	public int GetUserSize() {// 유저 수
		return userList.size();
	}

}
