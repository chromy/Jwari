/**
 * Uses the MinMax algorithm to pick the next move.
 * @author hjd11
 *
 */
public class MinMaxAI extends Mover {
	private static String movertype = "MinMaxAI";
	private int depth = 10;
	private int playerId;

	@Override
	public int getMove(Game game, String name) {
		playerId = game.getCurrentPlayerId();
		
		ScorePair score = minmax(game, depth);
		
		return score.getMove();
	}
	
	// Wikipedia & Computer Gamesmanship
	/**
	 * Recursive implementation of the minmax algorithm. Because we do the first
	 * level by hand in getMove all the values are inverted.  
	 */
	private ScorePair minmax(Game game, int depth) {
		if (depth <= 0 || game.isOver()) {
			return new ScorePair(-1, evaluation(game, playerId)).neg();
		}
		
		if (!game.canCurrentPlayerMove()) {
			Game copy_game = game.clone(); //Fix
			copy_game.swapPlayers();
			return minmax(copy_game, depth-1).neg();
		}
		
		int bestscore = Integer.MIN_VALUE;
		int bestmove = -1;
	    for (int move : game.allValidMoves()) {
	    	if (move >= 0) {
	    		int score = -minmax(game.afterMove(move), depth-1).getScore();
	    		if (bestscore < score) {
	    			bestscore = score;
	    			bestmove = move;
	    		}
	    	}
	    }
	    return new ScorePair(bestmove, bestscore);
		
		
	}
	
	private int evaluation(Game game, int currentplayer) {
		if (game.isOver()) {
			return (currentplayer == game.getLeadingPlayer() 
			                     ? Integer.MAX_VALUE : Integer.MIN_VALUE);
		}
		
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		return s1 - s2; 
	}
	
//	function integer minimax(node, depth)
//    if node is a terminal node or depth <= 0:
//        return the heuristic value of node
//    α = -∞
//    for child in node:                       # evaluation is identical for both players 
//        α = max(α, -minimax(child, depth-1))
//    return α

	public String getType() {
		return movertype;
	}
}
