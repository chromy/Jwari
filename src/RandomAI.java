import java.util.Random;

public class RandomAI extends Mover {
	private static String movertype = "Random";
	  
	private final Random random;

	public RandomAI(Random random) {
		this.random = random;
	}
	
	public int getMove(Game game, String name) {
		int move;
		do {
			move = random.nextInt(12);
		} while (!game.isValidMove(move));
		return move;
	}
	
	public String getType() {
		return movertype;
	}
}
