/**
 * The abstract class from which all 'AI' objects inherit.
 * @author hjd11
 */
public abstract class Mover {

	// Callers must guaranty to call with a game object from which there is at 
	// least one valid move for the current player.
	public abstract int getMove(Game game, String name);
	// Ugly hack so we can work out which AI we're dealing with.
	public abstract String getType();
  
}