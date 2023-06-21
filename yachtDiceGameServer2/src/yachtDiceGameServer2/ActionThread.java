package yachtDiceGameServer2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ActionThread extends Thread{
	
	private Socket sock;
	ArrayList<Socket> users = new ArrayList<Socket>();
	BufferedReader in = null;
	PrintStream out = null;
	RoomManager rManager = new RoomManager();
	GameLogic gameLogic = new GameLogic();
	String usersMsg;
	JSONObject jsonObject;
	JSONParser jsonParser = new JSONParser();
	
	
	public ActionThread(Socket sock, ArrayList<Socket> users,RoomManager rManager) {
		this.sock = sock;
		this.users = users;
		this.rManager = rManager;
	}
	
	@Override
	public void run() {
		
		try {
		while(true) {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			usersMsg = in.readLine();
			if(usersMsg == null) break;
			Object obj = jsonParser.parse(usersMsg);
			jsonObject = (JSONObject) obj;
			System.out.println(jsonObject.get("clickName"));
			
			String clickName = (String) jsonObject.get("clickName");
			String userName;
			boolean dice1Keep = true,dice2Keep = true,dice3Keep = true,dice4Keep = true,dice5Keep = true;
			
			
			
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
				
				System.out.println("각 주사위의 Keep값 : " + dice1Keep + " | " + dice2Keep + " | " + dice3Keep + " | " + dice4Keep
						+ " | " + dice5Keep);
				
				if(jsonObject.get("keepDice").equals("1")) {
					if(jsonObject.get("booleanKeep").equals(true)) {
						dice1Keep = false;
					}else if(jsonObject.get("booleanKeep").equals(false)) {
						dice1Keep = true;
					}
					
				}
				if(jsonObject.get("keepDice").equals(2)) {
					if(jsonObject.get("booleanKeep").equals(true)) {
						dice2Keep = false;
					}else if(jsonObject.get("booleanKeep").equals(false)) {
						dice2Keep = true;
					}
				}
				if(jsonObject.get("keepDice").equals(3)) {
					if(jsonObject.get("booleanKeep").equals(true)) {
						dice3Keep = false;
					}else if(jsonObject.get("booleanKeep").equals(false)) {
						dice3Keep = true;
					}
				}
				if(jsonObject.get("keepDice").equals(4)) {
					if(jsonObject.get("booleanKeep").equals(true)) {
						dice4Keep = false;
					}else if(jsonObject.get("booleanKeep").equals(false)) {
						dice4Keep = true;
					}
				}
				if(jsonObject.get("keepDice").equals(5)) {
					if(jsonObject.get("booleanKeep").equals(true)) {
						dice5Keep = false;
					}else if(jsonObject.get("booleanKeep").equals(false)) {
						dice5Keep = true;
					}
				}
				
				System.out.println("각 주사위의 바뀐 Keep값 : " + dice1Keep + " | " + dice2Keep + " | " + dice3Keep + " | " + dice4Keep
						+ " | " + dice5Keep);
				
				jsonObject = new JSONObject();
				jsonObject.put("clickName", "DiceKeepClick");
				jsonObject.put("dice1Keep", dice1Keep);
				jsonObject.put("dice2Keep", dice2Keep);
				jsonObject.put("dice3Keep", dice3Keep);
				jsonObject.put("dice4Keep", dice4Keep);
				jsonObject.put("dice5Keep", dice5Keep);
				
				
			}
			if(clickName.equals("Score")) {
				
			}
			if(clickName.equals("currentUser")) {
				
				
				
			}
			
			for(int l = 0; l < rManager.roomList.size(); l++) {
				System.out.println("roomManager.roomList.userList.size()" + rManager.roomList.get(l).userList.size());
				for(int j = 0; j < rManager.roomList.get(l).userList.size(); j++) {
					if(rManager.roomList.get(l).userList.get(j).getSock().equals(sock)) {
						int roomNum = rManager.roomList.get(l).getRoomNum();
						System.out.println("roomNum : " + roomNum);
						for(int k = 0; k < rManager.roomList.get(l).GetUserSize(); k++) {
							out = new PrintStream(rManager.roomList.get(l).userList.get(k).getSock().getOutputStream());
							
							if(clickName.equals("currentUser")) {
								if(k == 0) {
									
									jsonObject = new JSONObject();
									jsonObject.put("clickName", "current");
									
								}else if(k == 1){
									
									jsonObject = new JSONObject();
									jsonObject.put("clickName", "wait");
									
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
		}
		}
			catch(SocketException e2) {
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
		System.out.println("유저나감..");
		users.remove(sock);
	}

}
