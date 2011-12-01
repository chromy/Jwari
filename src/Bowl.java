
public class Bowl implements Cloneable {
	private int stones;
	private int deposited = 0;
	
	public Bowl(int stones) {
		this.stones = stones;
	}
	
	public Bowl clone() {
		try {
			return (Bowl)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
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
