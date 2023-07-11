package yachtDiceGameServer2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateRoom {
	
	BufferedReader in = null;
	PrintStream out = null;
	JSONObject jsonObject;
	JSONParser jsonParser = new JSONParser();
	String usersMsg;
	
	public void createAndEnterRoom(Socket sock,UserInfo user,YachtDiceRoom ydRoom,RoomManager rManager) {
		
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			usersMsg = in.readLine();
			Object obj = jsonParser.parse(usersMsg);
			jsonObject = (JSONObject) obj;
			
			String category = (String) jsonObject.get("category");
			System.out.println("받은 메세지의 카테고리 : " + category);
			
			if(category.equals("createRoom")) {
				
				int roomNum = Integer.parseInt(jsonObject.get("roomNum").toString());
				String userName = (String) jsonObject.get("userName");
				
				System.out.println("방 번호는 " + roomNum + " 이고, 유저 이름은 " + userName + " 입니다.");
				
				ydRoom = new YachtDiceRoom();
				
				user.setRoomNum(roomNum);
				user.setUser_name(userName);
				ydRoom.setRoomNum(roomNum);
				ydRoom.setRoomName(userName);
				
				user.enterRoom(ydRoom);
				ydRoom.enterUser(user);
				rManager.createRoom(ydRoom);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendRoomList(Socket sock, RoomManager rManager) {
		
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));			
			usersMsg = in.readLine();
			Object obj = jsonParser.parse(usersMsg);
			jsonObject = (JSONObject) obj;
			
			String category = (String) jsonObject.get("category");
			System.out.println("받은 메세지의 카테고리 : " + category);
			
			if(category.equals("getRoomList")) {
				
				ArrayList<RoomItem> item = new ArrayList<>();
				
				for(int l = 0; l < rManager.roomList.size(); l++) {
					
//					item.add(rManager.roomList.get(l).roomName,rManager.roomList.get(l).roomNum,rManager.roomList.get(l).GetUserSize());
					
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
