import java.util.ArrayDeque;

/** 
 * The Player class stores the score of the player and and allows the
 * score to increase.
 */
public class Player implements Cloneable {
	private ArrayDeque<Integer> history = new ArrayDeque<Integer>();
	private int score;
	
	public Player(int score) {
		this.score = score;
	}
	
	public Player() {
		score = 0;
	}
	
	public Player clone() {
		try {
			return (Player)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void restore() {
		assert (!history.isEmpty()) : "Can't restore any more scores.";
		score = history.pop();
	}
	
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
	 * @param score
	 */
	public void addToScore(int score) {
		assert (score >= 0) : "Score to add must be non-negative.";
		this.score += score;
	}
	

}
