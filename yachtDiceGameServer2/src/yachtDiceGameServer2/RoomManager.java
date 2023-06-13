package yachtDiceGameServer2;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
	
List<YachtDiceRoom> roomList; // 방의 리스트
	
	public RoomManager() {
		roomList = new ArrayList<YachtDiceRoom>();
	}
	
	public void createRoom(YachtDiceRoom room) {
		roomList.add(room);
		System.out.println("방이 방 리스트에 들어감");
	}
	
	public void removeRoom(YachtDiceRoom room) {
		roomList.remove(room);
		System.out.println("방을 삭제되었습니다.");
	}
	
	public int roomCount() {
		return roomList.size();
	}
	
	public int roomUserSize() {
		return roomList.get(roomList.size() - 1).GetUserSize();
	}

}
