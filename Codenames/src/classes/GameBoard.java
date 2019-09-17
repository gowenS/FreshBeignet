package classes;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class GameBoard {
	Random random;
	String card_color;
	String board;
	
	public GameBoard() {
		board = makeGameBoard();
	}
	
	private String makeGameBoard() {
		ArrayList<String> out = new ArrayList<>();
		random = new Random();
		int whose_turn = random.nextInt(100);
		if (whose_turn > 49) {
			card_color = "red";
			out.add("r");
		} else {
			card_color = "blue";
			out.add("b");
		}
		for(int i = 0;i<8;i++) {
			out.add("r");
			out.add("b");
		}
		for(int j = 0;j<7;j++) {
			out.add("n");
		}
		out.add("a");
		
		Collections.shuffle(out);
		return out.toString();
	}	
	
	public String getGameBoard() {
		return board;
	}
	
	public String getBoardColor() {
		return card_color;
	}
	
}
