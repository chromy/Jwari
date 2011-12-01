
public class Player implements Cloneable {
	
	private int score;
	
	public Player(int score) {
		this.score = score;
	}
	
	public Player clone() {
		try {
			return (Player)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public void addToScore(int score) {
		assert (score >= 0) : "Score to add must be non-negative.";
		this.score += score;
	}
	

}
