package yachtDiceGameServer2;

import java.util.Random;

public class GameLogic {
	
	public int rollDice() {
		
		Random roll = new Random();
		
		return roll.nextInt(6) + 1;
		
	}

}
