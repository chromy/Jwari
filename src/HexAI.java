import java.util.Arrays;

public class HexAI extends Mover {
	public static String movertype = "HexAI";
	private int playerId;
	private int bestmove;
	private int depth = 12;
	private int[] PV = new int[depth];
	// Moral of the day; -Integer.MIN_VALUE != Integer.MAX_VALUE *cry*
	private static int MIN_VALUE = -100000;
	private static int MAX_VALUE = 100000;
	
	
	@Override
	public String getType() {
		return movertype;
	}
	
	@Override
	public int getMove(Game game, String name) {
		playerId = game.getCurrentPlayerId();
		bestmove = -1;
		int score;
		score = alphabeta(game, depth, MIN_VALUE, MAX_VALUE, 1);
		
		if (score == MIN_VALUE) {
			System.out.println("HexAI: With good play I think I lose.");
		} else if (score == MAX_VALUE) {
			System.out.println("HexAI: With good play I think I win.");
		}
		
		System.out.println(java.util.Arrays.deepToString(PV));
		//assert (bestmove >= 0) : "getMove returned an invalid move.";
		return PV[0];
	}
	
	private int alphabeta(Game game, int depth, int alpha, int beta, int color) {
		int score;
		
		// So if we need to stop or if the game is over then return the score.
		if (depth <= 0 || game.isOver()) {
			// Quiescence search here?
			return color * evaluation(game, playerId);
		}
		
		// If the current player can't actually move then it's the other players
		// turn again.
		if (!game.canCurrentPlayerMove()) {
			game = game.clone(); //Fix
			game.swapPlayers();
			return -alphabeta(game, depth-1, -beta, -alpha, -color);
		}
		
		// Otherwise consider each move.
		for (int move : game.allValidMoves()) {
			if (move >= 0) {
				// Get the score of this move.
				score = -alphabeta(game.afterMove(move), depth-1, -beta, -alpha, -color);
				
				// Get move.
				if (score >= alpha) {
					PV[this.depth - depth] = move;
					alpha = score;
				}
				
				
				
				// AlphaBeta pruning.
				// Improve?
				if (beta <= alpha) {
					break;
				}
			}
		}
		return alpha;	
	}
	
	private int evaluation(Game game, int currentplayer) {
		if (game.isOver()) {
			return (currentplayer == game.getLeadingPlayer()) 
			                      ? MAX_VALUE : MIN_VALUE;
		}
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		// We're going to end up negating this depending on the player so 
		// it has to be symmetric
		return 100 + s1 - s2; 
	}

}
