import java.util.Random;
/**
 * A player who moves at random.
 * @author hjd11
 *
 */
public class RandomAI extends Mover {
	private static String movertype = "Random";
	private final Random random;

	public RandomAI(Random random) {
		this.random = random;
	}
	
	public RandomAI() {
		random = new Random();
		System.out.println(random.toString());
	}
	
	public int getMove(Game game, int opponentMove, String name) {
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
