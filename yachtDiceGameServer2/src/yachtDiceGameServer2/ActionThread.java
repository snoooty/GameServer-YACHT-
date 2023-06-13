package yachtDiceGameServer2;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ActionThread extends Thread{
	
	private Socket sock;
	ArrayList<Socket> users = new ArrayList<Socket>();
	BufferedReader in = null;
	PrintStream out = null;
	RoomManager rManager = new RoomManager();
	
	public ActionThread(Socket sock, ArrayList<Socket> users) {
		this.sock = sock;
		this.users = users;
	}
	
	@Override
	public void run() {
		
		
		
	}

}
