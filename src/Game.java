/**
 * Implements the game state object.
 * @author hjd11
 *
 */
public class Game implements Cloneable {
	private static final int numberofbowls = 12;
	private int halfway = (int)(numberofbowls/2); 
	private int currentplayerId = 0;
	private Player[] players;
	public Bowl[] bowls;
	
	/**
	 * Default constructor - make all our members by our self.
	 */
	public Game() {
		players = new Player[] {new Player(0), new Player(0)};
		bowls = new Bowl[12];
		for (int i=0; i<bowls.length; i++) {
			bowls[i] = new Bowl(4);
		}
	}
	
	/**
	 * Get a deep copy of self. 
	 * @return A deep copy of the this game.
	 */
	public Game clone() {
		try {
			Game copy = (Game)super.clone();
			copy.players = players.clone();
			for (int i=0; i<players.length; i++) {
				copy.players[i] = players[i].clone();
			}
			copy.bowls = bowls.clone();
			for (int i=0; i<bowls.length; i++) {
				copy.bowls[i] = bowls[i].clone();
			}
			return copy;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return The current playerId (either 0 or 1).
	 */
	public int getCurrentPlayerId() {
		return currentplayerId;
	}
	
	/**
	 * Gets a player by their playerId.
	 * @param playerId
	 * @return The player object with the id playerId. 
	 */
	public Player getPlayer(int playerId) {
		assert (playerId==0 || playerId==1) : "Player Id should be 0 or 1.";
		return players[playerId];
		
	}
	
	/**
	 * Changes the current player to be whoever the current player isn't.
	 * Doesn't update scores, flush bowls or make sure the new current player
	 * can actually move.   
	 */
	public void swapPlayers() {
		currentplayerId = (currentplayerId+1) % 2;
	}

	/**
	 * An array containing all the valid moves (0-11) for the current player
	 * listed once with the rest of the elements set to -1.  
	 * @return Array of all valid moves with remaining spaces set to -1.
	 */
	public int[] allValidMoves() {
		int shift = getCurrentPlayerId()*halfway;
		int[] result = new int[halfway];
		for (int i=0; i<halfway; i++) {
			int move = i + shift;
			result[i] = (isValidMove(move)) ? move : -1;
		}
		return result;
	}
	
	/**
	 * @param The number of a bowl (0-11).
	 * @return true iff that move is valid
	 */
	public boolean isValidMove(int bowl) {
		return (bowl>=0 
				&& bowl<bowls.length
				&& bowl>=getCurrentPlayerId()*halfway
				&& bowl<(getCurrentPlayerId()+1)*halfway
				&& bowls[bowl].getStones()>0);
	}
	
	/**
	 * Returns true if current player can make any move.
	 * @return true iff current player can make a move.
	 */
	public boolean canCurrentPlayerMove() {
		for (int i=0; i<bowls.length; i++) {
			if (isValidMove(i)) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Updates the state to reflect the given move taking place.
	 * Changes bowls state, updates player scores and swaps the players so the 
	 * next player is starting. Move must be valid. Swaps players even if the 
	 * player swapped to has no valid move.
	 * @param bowl number of bowl to 'move' (0-11).
	 */
	public void move(int bowl) {
		assert (isValidMove(bowl)) : "Move must be valid. Move was: " + bowl;
		int stones = bowls[bowl].takeAllStones();
		while (stones>0) {
			bowl++;
			bowl %= bowls.length;
			bowls[bowl].depositStone();
			stones--;
		}
		updateBowles();
		storeScores();
		swapPlayers();
	}
	
	public void undomove() {
		for (Bowl b : bowls) {
			b.restore();
		}
		for (Player p : players) {
			p.restore();
		}
		swapPlayers();
	}
	

	/**
	 * 
	 * @param moves
	 * @return
	 */
	public Game moveSequence(int[] moves) {
		Game game = clone();
		int m = 0;
		for (int i=0; i<moves.length; i++) {
			m = moves[i];
			if (game.isOver()) {
				break;
			} else if (m < 0) {
				break;
			} else if (!game.canCurrentPlayerMove()) {
				game.swapPlayers();
			} else {
				assert (game.isValidMove(m)) : "Move " + m + " in move sequence " + java.util.Arrays.toString(moves) + " is not valid.";
				game.move(m);
			}
		}
		return game;
	}
	
	/**
	 * Returns a new game object with the state of the current game object 
	 * updated by the given move.
	 * @param  bowl - The id (0-11) of the bowl which you want to move.
	 * @return A deep copy of this object that then makes the move bowl.
	 */
	public Game afterMove(int bowl) {
		assert (isValidMove(bowl)) : "Move must be valid. Move was: " + bowl;
		Game copy = this.clone();
		copy.move(bowl);
		return copy;	
	}
	
	/**
	 * Updates every bowl adding any captured stones to the current players 
	 * bowl.
	 */
	private void updateBowles() {
		int cp = getCurrentPlayerId();
		for (Bowl b : bowls) {
			players[cp].addToScore(b.updateAndGetScore());
		}
	}
	
	private void storeScores() {
		for (Player p : players) {
			p.store();
		}
	}
	
	/**
	 * 
	 * @return The id of the player with the highest score (either 0 or 1); 
	 * 		   returns 0 in the case of a draw. 
	 */
	public int getLeadingPlayer() {
		return (players[0].getScore() >= players[1].getScore()) ? 0 : 1 ;
	}
	
	/**
	 * The game is over if either of the players have a score >= 24.
	 * @return true iff one player has a score over or equal to 24. 
	 * 		   false otherwise.
	 */
	public boolean isOver() {
		return Math.max(players[0].getScore(), players[1].getScore()) >= 24;
	}
	
	/**
	 * Prints a graphical representation of the game straight to stdout.
	 */
	public void display() {
		assert (bowls.length % 2 == 0) : "Number of bowls must be even.";
		
		String line = ""; 
		
		System.out.println("Player 2: " + players[1].getScore());
		System.out.println();
		for (int i=bowls.length-1; i>halfway-1; i--) {
			line += String.format("  %2d   ", i+1);
		}
		System.out.println(line);
		line = "";
		
		for (int i=bowls.length-1; i>halfway-1; i--) {
			line += String.format(" (%2d ) ", bowls[i].getStones());
		}
		System.out.println(line);
		System.out.println();
		line = "";
		
		for (int i=0; i<halfway; i++) {
			line += String.format(" (%2d ) ", bowls[i].getStones());
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
