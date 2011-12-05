import java.util.Arrays;

public class HexAI extends Mover {
	public static String movertype = "HexAI";
	private int playerId;
	private int nodesSearched = 0;
	private int nodesEval = 0;
	private int nodesTerm = 0;
	private int depth = 18;
	private int[] PV = new int[depth];
	private int[] PV_SCORES = new int[depth];
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
		nodesSearched = 0;
		nodesEval = 0;
		nodesTerm = 0;
		PV = new int[depth];
		
		int score = 0;
		
		long t1 = System.currentTimeMillis();
		score = alphabeta(game, new int[this.depth], PV, 0, depth, MIN_VALUE, MAX_VALUE, 1);
		long t2 = System.currentTimeMillis();
		
		System.out.println("Nodes searched: " + nodesSearched);
		System.out.println("Nodes evaluated: " + nodesEval);
		System.out.println((100*nodesTerm/(float)nodesEval) + "% where final.");
		System.out.println("Current score: " + evaluation(game, playerId));
		System.out.println("Final score: " + score);
		System.out.println("PV: " + java.util.Arrays.toString(PV));
		System.out.println("PV Scores: " + java.util.Arrays.toString(PV_SCORES));
		nodesSearched = 0;
		nodesEval = 0;
		nodesTerm = 0;
		PV = new int[depth];
		
		System.out.println("---------------------------------");
		long t3 = System.currentTimeMillis();
		score = IDAlphaBeta(game, PV, depth);
		long t4 = System.currentTimeMillis();
		
		System.out.println("Nodes searched: " + nodesSearched);
		System.out.println("Nodes evaluated: " + nodesEval);
		System.out.println((100*nodesTerm/(float)nodesEval) + "% where final.");
		System.out.println("Current score: " + evaluation(game, playerId));
		System.out.println("Final score: " + score);
		System.out.println("PV: " + java.util.Arrays.toString(PV));
		System.out.println("PV Scores: " + java.util.Arrays.toString(PV_SCORES));
		
		System.out.println("Plain AB took: " + (t2-t1));
		System.out.println("IDAB took: " + (t4-t3));
		
		if (score == MIN_VALUE) {
			System.out.println("HexAI: With good play I think I lose.");
			System.out.println(exhaustSearch(game, 15, 1));
		} else if (score == MAX_VALUE) {
			System.out.println("HexAI: With good play I think I win.");
			System.out.println(exhaustSearch(game, 25, 1));
		}
		
		//assert (bestmove >= 0) : "getMove returned an invalid move.";
		return PV[0];
	}
	
	// iterative deepening
	private int IDAlphaBeta(Game game, int[] principleVar, int limit) {
		int depth = 0;
		
		int[] bestsofar = Arrays.copyOf(principleVar, limit);;
		int[] result = null;
		while (limit != depth+10) {
			result = new int[depth];
			alphabeta(game, bestsofar, result, 0, depth, MIN_VALUE, MAX_VALUE, 1);
			bestsofar =  Arrays.copyOf(result, depth);
			depth++;
		}
		
		result = new int[limit];
		int score = alphabeta(game, bestsofar, result, 0, limit, MIN_VALUE, MAX_VALUE, 1);
		for (int i=0; i<limit; i++) {
			principleVar[i] = result[i]; //Arrays.copyOf(result, limit);
		}
		return score;
	}
	
	private int alphabeta(Game game, 
			int[] principleVar,
			int[] result,
			int depth,
			int limit,
			int alpha,
			int beta, 
			int color) {
		
		int score;
		nodesSearched++;
		
		// So if we need to stop or if the game is over then return the score.
		if (depth == limit || game.isOver()) {
			// Quiescence search here?
			nodesEval++;
			return color * evaluation(game, playerId);
		}
		
		// If the current player can't actually move then it's the other players
		// turn again.
		if (!game.canCurrentPlayerMove()) {
			game = game.clone(); //Fix
			game.swapPlayers();
			return -alphabeta(game, principleVar, result, 
					depth+1, limit, -beta, -alpha, -color);
		}
		
		int pmove = -1;
		if (principleVar.length > depth) {
			pmove = principleVar[depth];
			if (pmove >= 0 && game.isValidMove(pmove)) {
				// Get the score of this move.
				score = -alphabeta(game.afterMove(pmove), principleVar, result,
						depth+1, limit, -beta, -alpha, -color);
				
				// Get move.
				if (score >= alpha) {
					result[depth] = pmove;
					PV_SCORES[depth] = evaluation(game, playerId);
					alpha = score;
				}
				
				// AlphaBeta pruning.
				// Improve?
				if (beta <= alpha) {
					return alpha;
				}
			}
		}
		
		// Otherwise consider each move.
		for (int move : game.allValidMoves()) {
			if (move >= 0 && move != pmove) {
				// Get the score of this move.
				score = -alphabeta(game.afterMove(move), principleVar, result,
						depth+1, limit, -beta, -alpha, -color);
				
				// Get move.
				if (score >= alpha) {
					result[depth] = move;
					PV_SCORES[depth] = evaluation(game, playerId);
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
	
	private int exhaustSearch(Game game, int limit, int color) {
		boolean indeter = false;
		
		// If we haven't found a solution by now we'll have to stop not
		// knowing the result.
		if (limit <= 0) {
			return 0;
		}
		
		// If the game is over then we return either 1 or -1. 
		// If we're the minimising (color = -1) player we return 1 if we're 
		// wining and -1. Vice versa for the maxamizing player 
		// (HexAI, color = 1).  
		if (game.isOver()) {
			return color * ((game.getLeadingPlayer() == playerId) ? 1 : -1);
		}
		
		// If we can't move the other player takes another move.
		if (!game.canCurrentPlayerMove()) {
			game.swapPlayers();
			return -exhaustSearch(game, limit-1, -color);
		}
		
		// If we can make a winning move then this variation wins! So we can 
		// return 1, (the negatives sort out whether thats good or bad for the 
		// maximising player). Otherwise if at least one of the moves is 
		// indeterminate then we might win so return 0. If all moves are losing 
		// then this variation loses.
		for (int move : game.allValidMoves()) {
			if (move >= 0) {
				int result = -exhaustSearch(game.afterMove(move), limit-1, -color);
				if (result == 1) {
					return 1;
				}
				
				if (result == 0) {
					indeter = true;
				}	
			}
		}
		return indeter ? 0 : -1;
	}
	
	
	private int evaluation(Game game, int currentplayer) {
		if (game.isOver()) {
			nodesTerm++;
			return (currentplayer == game.getLeadingPlayer()) 
			                      ? MAX_VALUE : MIN_VALUE;
		}
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		// We're going to end up negating this depending on the player so 
		// it has to be symmetric
		
		int LBFactor = 6;
		int largebowls = 0;
		for (int i=currentplayer*6; i<6+currentplayer*6; i++) {
			if (game.bowls[i].getStones() >= 8) {
				largebowls++;
			}
		}
		int OBFactor = 1;
		int onebowls = 0;
		for (int i=((currentplayer+1)%2)*6; i<6+((currentplayer+1)%2)*6; i++) {
			if (game.bowls[i].getStones() == 1) {
				onebowls++;
			}
		}
		return 100 + s1 - s2 + LBFactor*largebowls + OBFactor*onebowls; 
	}

}
