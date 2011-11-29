
public class Game {
	private int currentplayerId = 0;
	public Player[] players;
	private final Bowl[] bowles;
	private int halfway;
	
	public Game() {
		players = new Player[] {new Player(), new Player()};
		bowles = new Bowl[12];
		halfway = (int)(bowles.length/2);
		for (int i=0; i<bowles.length; i++) {
			bowles[i] = new Bowl(4);
		}
		
	}
	
	public int getCurrentPlayerId() {
		return currentplayerId;
	}
	
	public void swapPlayers() {
		currentplayerId = (currentplayerId+1) % 2;
	}
	
	public int[] allValidMoves() {
		int shift = getCurrentPlayerId()*halfway;
		int[] result = new int[halfway];
		for (int i=0; i<halfway; i++) {
			int move = i + shift;
			result[i] = (isValidMove(move)) ? move : -1;
		}
		return result;
	}
	
	public boolean isValidMove(int bowl) {
		return (bowl>=0 
				&& bowl<bowles.length
				&& bowl>=getCurrentPlayerId()*halfway
				&& bowl<(getCurrentPlayerId()+1)*halfway
				&& bowles[bowl].getStones()>0);
	}
	
	public boolean canCurrentPlayerMove() {
		for (int i=0; i<bowles.length; i++) {
			if (isValidMove(i)) {
				return true;
			}
		}
		return false;
		
	}
	
	public void move(int bowl) {
		assert (isValidMove(bowl)) : "Move must be valid. Move was: " + bowl;
		int stones = bowles[bowl].takeAllStones();
		while (stones>0) {
			bowl++;
			bowl %= bowles.length;
			bowles[bowl].depositStone();
			stones--;
		}
		updateBowles();
		swapPlayers();
	}
	
	
	
	private void updateBowles() {
		int cp = getCurrentPlayerId();
		for (Bowl b : bowles) {
			players[cp].addToScore(b.updateAndGetScore());
		}
	}
	
	// If draw return 0.
	public int getLeadingPlayer() {
		return (players[0].getScore() >= players[1].getScore()) ? 0 : 1 ;
	}
	
	public boolean isOver() {
		return Math.max(players[0].getScore(), players[1].getScore()) >= 24;
		
	}
	
	public void display() {
		assert (bowles.length % 2 == 0) : "Number of bowls must be even.";
		
		String line = ""; 
		
		System.out.println("Player 2: " + players[1].getScore());
		System.out.println();
		for (int i=bowles.length-1; i>halfway-1; i--) {
			line += String.format("  %2d   ", i+1);
		}
		System.out.println(line);
		line = "";
		
		for (int i=bowles.length-1; i>halfway-1; i--) {
			line += String.format(" (%2d ) ", bowles[i].getStones());
		}
		System.out.println(line);
		System.out.println();
		line = "";
		
		for (int i=0; i<halfway; i++) {
			line += String.format(" (%2d ) ", bowles[i].getStones());
		}
		System.out.println(line);
		line = "";
		
		for (int i=0; i<halfway; i++) {
			line += String.format("  %2d   ", i+1);
		}
		System.out.println(line);
		System.out.println();
		System.out.println("Player 1: " + players[0].getScore());
		
	}
	
}
