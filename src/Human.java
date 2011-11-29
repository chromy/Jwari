
public class Human extends Mover {
	public static String movertype = "Human";
	  
	public int getMove(Game game, String name) {
		System.out.println(name + ": Please enter your move.");
		int move = IOUtil.readInt()-1;
		
		while (!game.isValidMove(move)) {
			System.out.println("That's not a valid move. Please try again.");
			move = IOUtil.readInt()-1;
		} ;
		return move;
	}
	
	public String getType() {
		return movertype;
	}
}
