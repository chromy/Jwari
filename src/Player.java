
public class Player {
	
	private int score = 0;
	
	public int getScore() {
		return score;
	}
	
	public void addToScore(int score) {
		assert (score >= 0) : "Score to add must be non-negative.";
		this.score += score;
	}
	

}
