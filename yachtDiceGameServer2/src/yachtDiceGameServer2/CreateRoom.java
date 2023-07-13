package yachtDiceGameServer2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateRoom {
	
	BufferedReader in = null;
	PrintStream out = null;
	JSONObject jsonObject;
	JSONParser jsonParser = new JSONParser();
	String usersMsg;
	
	
	public void sendRoomList(Socket sock, RoomManager rManager,UserInfo user,YachtDiceRoom ydRoom) {
		
		new Thread(() -> {
			
			while(true) {
				
				try {
					in = new BufferedReader(new InputStreamReader(sock.getInputStream()));	
					out = new PrintStream(sock.getOutputStream());
					usersMsg = in.readLine();
					Object obj = jsonParser.parse(usersMsg);
					jsonObject = (JSONObject) obj;
					
					
					
					
					
					
				}catch(SocketException e2) {
					System.out.println("Send : 상대방 연결이 종료되었습니다.");
					e2.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException");
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("파서에러");
					e.printStackTrace();
				}
				
			}			
		}).start();		
	}
	
}
