package yachtDiceGameServer2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PlayGameThread extends Thread{
	
	Socket sock;
	RoomManager rManager;
	YachtDiceRoom ydRoom;
	UserInfo user;
	String usersMsg;
	BufferedReader in = null;
	PrintStream out = null;
	JSONObject jsonObject;
	JSONParser jsonParser = new JSONParser();
	GameLogic gameLogic = new GameLogic();
	String player1,player2;
	
	public PlayGameThread(Socket sock,RoomManager rManager,UserInfo user,YachtDiceRoom ydRoom) {
		this.sock = sock;
		this.rManager = rManager;
		this.user = user;
		this.ydRoom = ydRoom;
	}
	
	@Override
	public void run() {
		
		try {
			
			while(true) {
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				usersMsg = in.readLine();
				Object obj = jsonParser.parse(usersMsg);
				jsonObject = (JSONObject) obj;
				
				System.out.println("clickName 값 : " + jsonObject.get("clickName"));
				
				String clickName = (String) jsonObject.get("clickName");
				String category = (String) jsonObject.get("category");
				System.out.println("send RoomList 받은 메세지의 카테고리 : " + category);
				
				if(clickName == null) {
					clickName = "비어있음";
				}
				if(category == null) {
					category = "값이 없음";
				}
				
				String userName;
				String diceClick = "";
				boolean dice1Keep = true,dice2Keep = true,dice3Keep = true,dice4Keep = true,dice5Keep = true;
				
				if(rManager != null && category.equals("getRoomList")) {
					
					out = new PrintStream(sock.getOutputStream());
					
					JSONArray jArray = new JSONArray();
									
					for(int l = 0; l < rManager.roomList.size(); l++) {
						
						JSONObject sObject = new JSONObject();
						sObject.put("roomName", rManager.roomList.get(l).roomName);
						sObject.put("roomNum", rManager.roomList.get(l).roomNum);
						sObject.put("personner", rManager.roomList.get(l).GetUserSize());
						jArray.add(sObject);
						
					}
					
					jsonObject = new JSONObject();
					jsonObject.put("category", "getRoomList");
					jsonObject.put("List", jArray);						
					
					System.out.println("보낼 json값 : " + jsonObject.toString());
					out.println(jsonObject.toString());
					out.flush();
					
				}else if(category.equals("getRoomList")){
					
					out = new PrintStream(sock.getOutputStream());
					
					jsonObject = new JSONObject();
					jsonObject.put("category", "getRoomList");
					jsonObject.put("List", "없음");
					
					System.out.println("보낼 json값 : " + jsonObject.toString());
					out.println(jsonObject.toString());
					out.flush();
					
				}
				
				if(category.equals("createRoom")) {
					
					out = new PrintStream(sock.getOutputStream());
					
					int roomNum = Integer.parseInt(jsonObject.get("roomNum").toString());
					String roomName = (String) jsonObject.get("userName");
					
					System.out.println("방 번호는 " + roomNum + " 이고, 유저 이름은 " + roomName + " 입니다.");						
					
					user.setRoomNum(roomNum);
					user.setUser_name(roomName);
					ydRoom.setRoomNum(roomNum);
					ydRoom.setRoomName(roomName);
					
					user.enterRoom(ydRoom);
					ydRoom.enterUser(user);
					rManager.createRoom(ydRoom);
					
					jsonObject = new JSONObject();
					jsonObject.put("category", "createRoom");
					out.println(jsonObject.toString());
					out.flush();
				}
				
				if(category.equals("enterRoom")) {
					
					out = new PrintStream(sock.getOutputStream());
					
					int roomNum = Integer.parseInt(jsonObject.get("roomNum").toString());
					System.out.println("입장하는 방번호는 " + roomNum + " 입니다.");
					
					user.setRoomNum(roomNum);
					
					for (int i = 0; i < rManager.roomList.size(); i++) {
						
						if(rManager.roomList.get(i).getRoomNum() == roomNum) {
							user.enterRoom(rManager.roomList.get(i));
							rManager.roomList.get(i).enterUser(user);
						}						
					}
					
					jsonObject = new JSONObject();
					jsonObject.put("category","enterRoom");
					out.println(jsonObject.toString());
					out.flush();
					
				}
				
				if(clickName.equals("DiceRollClick")) {// user가 diceRoll 클릭 시 반응
					
					
					System.out.println("json으로 받은 id : " + jsonObject.get("userName"));
					System.out.println("주사위 카운트 : " + jsonObject.get("count"));
					
					userName = (String) jsonObject.get("userName");
					
					jsonObject = new JSONObject();
					jsonObject.put("clickName", "diceRollClick");
					jsonObject.put("rollUser", userName);
					
					// 주사위 굴리기
					if(dice1Keep) {
						jsonObject.put("dice1", gameLogic.rollDice());
					}
					if(dice2Keep) {
						jsonObject.put("dice2", gameLogic.rollDice());
					}
					if(dice3Keep) {
						jsonObject.put("dice3", gameLogic.rollDice());
					}
					if(dice4Keep) {
						jsonObject.put("dice4", gameLogic.rollDice());
					}
					if(dice5Keep) {
						jsonObject.put("dice5", gameLogic.rollDice());
					}
					
					System.out.println("주사위 굴려서 보내기");
					System.out.println(jsonObject.toString());
					
					
				}
				if(clickName.equals("DiceKeepClick")) {// user가 diceKeep 클릭 시 반응
					
//					System.out.println("각 주사위의 Keep값 : " + dice1Keep + " | " + dice2Keep + " | " + dice3Keep + " | " + dice4Keep
//							+ " | " + dice5Keep);
					
					if(jsonObject.get("booleanKeep1").equals(true)) {
						dice1Keep = true;
					}else if(jsonObject.get("booleanKeep1").equals(false)) {
						dice1Keep = false;
					}
					if(jsonObject.get("booleanKeep2").equals(true)) {
						dice2Keep = true;
					}else if(jsonObject.get("booleanKeep2").equals(false)) {
						dice2Keep = false;
					}
					if(jsonObject.get("booleanKeep3").equals(true)) {
						dice3Keep = true;
					}else if(jsonObject.get("booleanKeep3").equals(false)) {
						dice3Keep = false;
					}
					if(jsonObject.get("booleanKeep4").equals(true)) {
						dice4Keep = true;
					}else if(jsonObject.get("booleanKeep4").equals(false)) {
						dice4Keep = false;
					}
					if(jsonObject.get("booleanKeep5").equals(true)) {
						dice5Keep = true;
					}else if(jsonObject.get("booleanKeep5").equals(false)) {
						dice5Keep = false;
					}
					
					
					if(Integer.parseInt(jsonObject.get("keepDice").toString()) == 1) {
						diceClick = "dice1";
						if(jsonObject.get("booleanKeep1").equals(true)) {
							dice1Keep = false;
						}else if(jsonObject.get("booleanKeep1").equals(false)) {
							dice1Keep = true;
						}
						
					}
					if(Integer.parseInt(jsonObject.get("keepDice").toString()) == 2) {
						diceClick = "dice2";
						if(jsonObject.get("booleanKeep2").equals(true)) {
							dice2Keep = false;
						}else if(jsonObject.get("booleanKeep2").equals(false)) {
							dice2Keep = true;
						}
					}
					if(Integer.parseInt(jsonObject.get("keepDice").toString()) == 3) {
						diceClick = "dice3";
						if(jsonObject.get("booleanKeep3").equals(true)) {
							dice3Keep = false;
						}else if(jsonObject.get("booleanKeep3").equals(false)) {
							dice3Keep = true;
						}
					}
					if(Integer.parseInt(jsonObject.get("keepDice").toString()) == 4) {
						diceClick = "dice4";
						if(jsonObject.get("booleanKeep4").equals(true)) {
							dice4Keep = false;
						}else if(jsonObject.get("booleanKeep4").equals(false)) {
							dice4Keep = true;
						}
					}
					if(Integer.parseInt(jsonObject.get("keepDice").toString()) == 5) {
						diceClick = "dice5";
						if(jsonObject.get("booleanKeep5").equals(true)) {
							dice5Keep = false;
						}else if(jsonObject.get("booleanKeep5").equals(false)) {
							dice5Keep = true;
						}
					}
					
					System.out.println("각 주사위의 바뀐 Keep값 : " + dice1Keep + " | " + dice2Keep + " | " + dice3Keep + " | " + dice4Keep
							+ " | " + dice5Keep);
					
					jsonObject = new JSONObject();
					jsonObject.put("clickName", "DiceKeepClick");
					jsonObject.put("diceClick", diceClick);
					jsonObject.put("dice1Keep", dice1Keep);
					jsonObject.put("dice2Keep", dice2Keep);
					jsonObject.put("dice3Keep", dice3Keep);
					jsonObject.put("dice4Keep", dice4Keep);
					jsonObject.put("dice5Keep", dice5Keep);
					
					
				}
				if(clickName.equals("ScoreClick")) {
					
					String scoreName = (String) jsonObject.get("scoreName");
					String player = (String) jsonObject.get("player");
					int score = Integer.parseInt(String.valueOf(jsonObject.get("score")));
					
					jsonObject = new JSONObject();
					jsonObject.put("clickName", "ScoreClick");
					jsonObject.put("scoreName", scoreName);
					jsonObject.put("player", player);
					jsonObject.put("score", score);
					
				}
				if(clickName.equals("userName")) {
					
					jsonObject = new JSONObject();
					jsonObject.put("userName", "userName");
					jsonObject.put("player1", player1);
					
				}
				
				for(int l = 0; l < rManager.roomList.size(); l++) {
					
					System.out.println("전체 방 roomNum : " + rManager.roomList.get(l).roomNum);
					System.out.println("현재 유저 roomNum : " + user.roomNum);
					
					if(rManager.roomList.get(l).roomNum == user.roomNum) {
						
						for(int k = 0; k < rManager.roomList.get(l).GetUserSize(); k++) {
							
							out = new PrintStream(rManager.roomList.get(l).userList.get(k).getSock().getOutputStream());
							
							if(rManager.roomList.get(l).userList.get(0).getUser_name() == null) {
								rManager.roomList.get(l).userList.get(0).setUser_name((String) jsonObject.get("userName"));
							}
							if(rManager.roomList.get(l).userList.size() == 2) {
								if(rManager.roomList.get(l).userList.get(1).getUser_name() == null) {
									rManager.roomList.get(l).userList.get(1).setUser_name((String) jsonObject.get("userName"));
								}
							}
							
							if(clickName.equals("currentUser")) {
								if(k == 0) {
									
									jsonObject = new JSONObject();
									jsonObject.put("clickName", "current");
									jsonObject.put("player1", rManager.roomList.get(l).userList.get(0).getUser_name());
									if(rManager.roomList.get(l).userList.size() == 2) {
										jsonObject.put("player2", rManager.roomList.get(l).userList.get(1).getUser_name());
									}
								}else if(k == 1){
									
									jsonObject = new JSONObject();
									jsonObject.put("clickName", "wait");
									jsonObject.put("player1", rManager.roomList.get(l).userList.get(0).getUser_name());
									jsonObject.put("player2", rManager.roomList.get(l).userList.get(1).getUser_name());
									
								}
							}
							
							if(in != null) {
								System.out.println("보내기 전 json : " + jsonObject.toString());
								out.println(jsonObject.toString());
								out.flush();
								System.out.println("클라이언트로 메세지 보냈나?");
							}
							
						}
						
					}
				
				}
			}
			
			
			
		}catch(SocketException e2) {
			System.out.println("Send : 상대방 연결이 종료되었습니다.");
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("파서에러");
			e.printStackTrace();
		}
		
	}

}
