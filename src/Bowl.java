import java.util.ArrayDeque;

/**
 * A 'bowl' of stones that can have stones added to it, captured from it, be 
 * cloned and can keep a history of the number of stones in it. Importantly any
 * stones which are deposited but not yet either captured or added to the number
 * of stones are not guaranteed to be preserved by most methods. Essentially you
 * should call <code>depositStone()</code> n times then 
 * <code>updateAndGetScore()</code> before anything else.
 * @author hjd11
 */
public class Bowl implements Cloneable {
	private int stones;
	private int deposited = 0;
	private ArrayDeque<Integer> history = new ArrayDeque<Integer>();
	
	/**
	 * Creates a new Bowl with the given, non-negative number of stones.
	 * @param Inital number of stones.
	 */
	public Bowl(int stones) {
		assert(stones >= 0) : 
			"Bowls must start with a non-negative number of stones.";
		this.stones = stones;
		store();
	}
	
	/**
	 * @return A deep copy of ourselves.
	 */
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
	
	/**
	 * Add the current number of stones in the bowl to the stack.
	 */
	public void store() {
		history.push(stones);
	}
	
	/**
	 * Discard the current number of stones in the bowl and replace it with the
	 * last value from the stack. You can't restore more values than you store.
	 */
	public void restore() {
		assert (!history.isEmpty()) : "Can't restore any more scores.";
		stones = history.pop();
	}
	
	/**
	 * @return The number of stones in the bowl.
	 */
	public int getStones() {
		return stones;
	}
	
	/**
	 * Removes all stones from the bowl;
	 * @return The number of stones in the bowl.
	 */
	public int takeAllStones() {
		int takenstones = stones;
		stones = 0;
		return takenstones;
	}
	
	/**
	 * Add one stone to the number deposited. This will not be reflected in the
	 * value of <code>getStones()</code> until <code>updateAndGetScore()</code>
	 * is called.
	 */
	public void depositStone() {
		deposited++;
	}
	
	/**
	 * If there was one stone in the bowl and at least one deposited stone all 
	 * stones are captured and the number of stones (and deposited stones) is 
	 * set to zero. Otherwise all the deposited stones are added to the stones 
	 * in the bowl.
	 * @return Number of stones captured.
	 */
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
