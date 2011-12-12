import java.util.ArrayDeque;

/** 
 * The Player class stores the score of the player and and allows the
 * score to increase.
 */
public class Player implements Cloneable {
	private ArrayDeque<Integer> history = new ArrayDeque<Integer>(30);
	private int score;
	
	public Player(int score) {
		assert (score >= 0) : "Inital player score must be greater than 0.";
		this.score = score;
	}
	
	public Player() {
		score = 0;
	}
	
	/**
	 * @return A deep copy of ourselves.
	 */
	public Player clone() {
		try {
			Player copy = (Player)super.clone();
			copy.history = history.clone();
			return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Discards the current score to the recorded values.
	 */
	public void restore() {
		assert (!history.isEmpty()) : "Can't restore any more scores.";
		score = history.pop();
	}
	
	/**
	 * Adds the current score to the recorded values.
	 */
	public void store() {
		history.push(score);
	}
	
	/**
	 * @return The players score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Add the given integer to score;
	 * @param Number to be added to score.
	 */
	public void addToScore(int score) {
		assert (score >= 0) : "Score to add must be non-negative.";
		this.score += score;
	}
	

}
