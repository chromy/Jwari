
public final class ScorePair implements Comparable<ScorePair> {
	private final int move;
	private final int score;
	
	public static ScorePair min(ScorePair a, ScorePair b) {
		return a.compareTo(b) <= 0 ? a : b; 
	}
	
	public static ScorePair max(ScorePair a, ScorePair b) {
		return a.compareTo(b) >= 0 ? a : b; 
	}
	
	public ScorePair(int move, int score) {
		this.score = score;
		this.move = move;
	}
	
	public int getMove() {
		return move;
	}
	
	public int getScore() {
		return score;
	}
	
	public ScorePair neg() {
		return new ScorePair(-score, move);
	}

	@Override
	public int compareTo(ScorePair o) {
		return score - o.score;
	}
}
