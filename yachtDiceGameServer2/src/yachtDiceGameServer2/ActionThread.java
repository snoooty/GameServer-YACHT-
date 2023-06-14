package yachtDiceGameServer2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ActionThread extends Thread{
	
	private Socket sock;
	ArrayList<Socket> users = new ArrayList<Socket>();
	BufferedReader in = null;
	PrintStream out = null;
	RoomManager rManager = new RoomManager();
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
			
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			usersMsg = in.readLine();
			Object obj = jsonParser.parse(usersMsg);
			jsonObject = (JSONObject) obj;
			System.out.println(jsonObject.get("clickName"));
			
			String clickName = (String) jsonObject.get("clickName");
			
			if(clickName.equals("DiceRollClick")) {// user가 diceRoll 클릭 시 반응
				System.out.println(jsonObject.get("userName"));
				System.out.println(jsonObject.get("count"));
				
			}else if(clickName.equals("Dice1KeepClick")) {// user가 diceKeep 클릭 시 반응
				
			}else if(clickName.equals("Score")) {
				
			}
			
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
