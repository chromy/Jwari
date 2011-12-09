import java.util.ArrayDeque;


public class Bowl implements Cloneable {
	private int stones;
	private int deposited = 0;
	private ArrayDeque<Integer> history = new ArrayDeque<Integer>();
	
	public Bowl(int stones) {
		this.stones = stones;
		store();
	}
	
	public Bowl clone() {
		try {
			Bowl copy = (Bowl)super.clone();
			copy.history = history.clone();
			return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void store() {
		history.push(stones);
	}
	
	public void restore() {
		assert (!history.isEmpty()) : "Can't restore any more scores.";
		stones = history.pop();
	}
	
	public int getStones() {
		return stones;
	}
	
	public int takeAllStones() {
		int takenstones = stones;
		stones = 0;
		return takenstones;
	}
	
	public void depositStone() {
		deposited++;
	}
	
	public int updateAndGetScore() {
		int score = 0;
		if (stones == 1 && deposited > 0) {
			score = deposited + takeAllStones();	
		} else {
			stones += deposited;
		}
		deposited = 0;
		return score; 
	}
}
