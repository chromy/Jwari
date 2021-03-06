import java.util.Arrays;


public class HexAI extends Mover {
	public static String movertype = "HexAI";
	
	// Stats.
	private int playerId;
	private int nodesSearched = 0;
	private int nodesEval = 0;
	private int nodesTerm = 0;
	private int depthReached = 0;
	
	
	private final int maxDepth = 30;
	private int[] PV;
	private int[][] PVTri = makeTriArray(maxDepth+1);
	
	// Moral of the day; -Integer.MIN_VALUE != Integer.MAX_VALUE *cry*
	private static final int MIN_VALUE = -100000;
	private static final int MAX_VALUE =  100000;
	private static final int WIN       =  10000;
	private static final int LOSS      = -10000;
	private static final int QWIN      =  1000;
	private static final int QLOSS     = -1000;
	// Time for each move (in ms).
	private final long timeBudget = 10*1000;
	// True iff we've proved we can force a win.
	private boolean forcewin = false;
	
	
	@Override
	public String getType() {
		return movertype;
	}
	
	/**
	 * 
	 */
	@Override
	public int getMove(Game game, int opponentMove, String name) {
		playerId = game.getCurrentPlayerId();
		int opponentMoveGuess = PVTri[0][1];
		int depthReachedLastTurn = depthReached;
		//PVTri[0] = Arrays.copyOfRange(PVTri[0], 2, maxDepth);
		long finishTime = System.currentTimeMillis() + timeBudget;
		clearStats(); 
		int score = 0;
		
		
		if (!forcewin) {
			long t3 = System.currentTimeMillis();
			score = IDalphabeta(game, PVTri, 5, finishTime);
			long t4 = System.currentTimeMillis();
			PV = PVTri[0];
			
			System.out.println("IDAB took: " + (t4-t3));
			System.out.println("Time remaining in budget: " 
					+ (finishTime-System.currentTimeMillis()) + "ms");
			printStats(evaluation(game, playerId), score);
			
		} else {
			score = exhaustSearch(game, PVTri, 10, 1);
			PV = PVTri[0];
		}
		
		if (score == LOSS || score == WIN) {
			if (score == LOSS) {
				System.out.println("HexAI: With good play I think I lose...");
			} else {
				System.out.println("HexAI: With good play I think I win...");
			}
			
			long t1 = System.currentTimeMillis();
			score = exhaustSearch(game, PVTri, 10, 1);
			long t2 = System.currentTimeMillis();
			
			switch (score) {
			case -1:
				System.out.println("...exhaustive tests confirm loss can be forced.");
				break;
			case 1:
				System.out.println("...exhaustive tests confirm I can force win.");
				//forcewin = true;
				break;
			case 0:
				System.out.println("...exhaustive tests failed to exhaust.");
				break;
			}
			
			System.out.println("Calculation took: " + (t2-t1) + "ms");
		}

//		System.out.println("---------------------------------");

		//game.display();
		if (!game.isValidMove(PV[0])) {
			System.out.println("Error!");
			for (int m : game.allValidMoves()) {
				if (m >= 0) {
					return m;
				}
			}
		}
		
		
		
		System.out.println(Arrays.toString(PV));
		assert (game.isValidMove(PV[0])) : "getMove returned an invalid move.";
		Game afterPV = game.moveSequence(Arrays.copyOf(PV, Math.max(0, depthReached)));
		System.out.println("------------AFTER_PV-------------");
		afterPV.display();
		System.out.println("---------------------------------");
		
		//
		return PV[0];
	}
	
	private int[][] makeTriArray(int size) {
		int[][] triarray = new int[size][];
		for (int i=0; i<size; i++) {
			triarray[i] = new int[size-i-1];
		}
		return triarray;
	}
	
	private void clearStats() {
		depthReached = 0;
		nodesSearched = 0;
		nodesEval = 0;
		nodesTerm = 0;
	}
	
	private void printStats(int currentscore, int finalscore) {
		System.out.println("Nodes searched: " + nodesSearched);
		System.out.println("Nodes evaluated: " + nodesEval);
		System.out.println((100*nodesTerm/(float)nodesEval) + "% where final.");
		System.out.println("Current score: " + (currentscore-100));
		System.out.println("Final score: " + (finalscore-100));
		System.out.println("PV: " + java.util.Arrays.toString(PV));
	}
	
	// iterative deepening
	private int IDalphabeta(Game game, int[][] principleVar, int depth, long finishTime) {
		depthReached = depth;
		int score = alphabeta(game, principleVar, 0, depth, MIN_VALUE, MAX_VALUE, 1);
		
		if (finishTime > System.currentTimeMillis() && maxDepth > depth) {
			return IDalphabeta(game, principleVar,  depth+1, finishTime);
		}
		
		return score;
	}
	
	private int alphabeta(Game game, 
			int[][] principleVar,
			int depth,
			int limit,
			int alpha,
			int beta, 
			int color) {
		
		int score;
		nodesSearched++;
		
		// If the game is over then return the score.
		if (game.isOver()) {
			nodesEval++;
			return color * evaluation(game, playerId);
		}
		
		// So if we need to stop 
		if (depth == limit) {
			// Quiescence search?
			nodesEval++;
			return color * qsearch(game, playerId, 4);
			//return color * evaluation(game, playerId); //qsearch(game, playerId, 4);
		}
		
		Arrays.fill(principleVar[depth], -1);
		
		// If the current player can't actually move then it's the other players
		// turn again.
		if (!game.canCurrentPlayerMove()) {
			game.swapPlayers();
			score = -alphabeta(game, principleVar, depth+1, 
							   limit, -beta, -alpha, -color);
			copyandinsert(principleVar[depth], principleVar[depth+1], limit-depth-1, -2);
			game.swapPlayers();
			return score;
		}
		
		int pmove = -1; // principleVar[0][depth];
		/*
		if (pmove >= 0 && game.isValidMove(pmove)) {
			// Note to self: DON'T TOUCH
			
			// Get the score of this move.
			//score = -alphabeta(game.afterMove(move), principleVar,
			//				   depth+1, limit, -beta, -alpha, -color);
			game.move(pmove);
			score = -alphabeta(game, principleVar, depth+1, limit, 
					           -beta, -alpha, -color);
			game.undomove();
			
			//AlphaBeta pruning.
			//Improve?
			if (score >= beta) {
				return beta;
			}			

			// Record move.
			if (score > alpha) {
				copyandinsert(principleVar[depth], principleVar[depth+1], limit-depth-1, pmove);
				alpha = score;
			}
		}
		*/
		// Otherwise consider each move.
		for (int move : game.allValidMoves()) {
			if (move >= 0 && move != pmove) {
				// Note to self: DON'T TOUCH
				
				// Get the score of this move.
				//score = -alphabeta(game.afterMove(move), principleVar,
				//				   depth+1, limit, -beta, -alpha, -color);
				game.move(move);
				score = -alphabeta(game, principleVar, depth+1, limit, 
						           -beta, -alpha, -color);
				game.undomove();
				
				//AlphaBeta pruning.
				//Improve?
				if (score >= beta) {
					return beta;
				}			

				// Record move.
				if (score > alpha) {
					copyandinsert(principleVar[depth], principleVar[depth+1], limit-depth-1, move);
					alpha = score;
				
					//game.display();
					//System.out.println(Arrays.toString(principleVar[0]));
					//game.afterMove(move).moveSequence(Arrays.copyOf(thevar, limit-depth-1));
					//game.moveSequence(principleVar[depth]);
				    
				}
			}
		}
		return alpha;	
	}
	
	private int exhaustSearch(Game game, int[][] principleVar, int limit, int color) {
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
			return -exhaustSearch(game, principleVar, limit-1, -color);
		}
		
		// If we can make a winning move then this variation wins! So we can 
		// return 1, (the negatives sort out whether thats good or bad for the 
		// maximising player). Otherwise if at least one of the moves is 
		// indeterminate then we might win so return 0. If all moves are losing 
		// then this variation loses.
		for (int move : game.allValidMoves()) {
			if (move >= 0) {
				int result = -exhaustSearch(game.afterMove(move), principleVar, limit-1, -color);
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
	
	private int qsearch(Game game, int currentplayer, int depth) {
		int score = 0;
		if (game.isOver()) {
			return (currentplayer == game.getLeadingPlayer()) ? QWIN : QLOSS;
		}
		
		if (depth == 0) {
			return evaluation(game, currentplayer);
		}
		
		if (!game.canCurrentPlayerMove()) {
			game.swapPlayers();
			score = qsearch(game, currentplayer, depth-1);
			game.swapPlayers();
			return score;
		}
		
		int biggestPotId = -1;
		int smallestPotId = -1;
		int biggestPotStones = 0;
		int smallestPotStones = 48;
		int stones = 0;
		for (int m : game.allValidMoves()) {
			if (m >= 0) {
				stones = game.bowls[m].getStones();
				if (stones >= biggestPotStones) {
					biggestPotStones = stones;
					biggestPotId = m;
				}
				if (stones < smallestPotStones) {
					smallestPotStones = stones;
					smallestPotId = m;
				}
			}
		}
		
		game.move(biggestPotId);
		if (game.isOver()) {
			game.undomove();
			return (currentplayer == game.getLeadingPlayer()) ? QWIN : QLOSS; 
		}
		game.undomove();
		
		game.move(smallestPotId);
		score = qsearch(game, currentplayer, depth-1);
		game.undomove();
		return score;
	}
	
	private int evaluation(Game game, int currentplayer) {
		if (game.isOver()) {
			nodesTerm++;
			return (currentplayer == game.getLeadingPlayer()) 
			                      ? WIN : LOSS;
		}
		
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		return 100 + s1 - s2;
	}
	
	
	public static void copyandinsert(int[] l1, int[] l2, int n, int e) {
		for (int i=0; i<n; i++) {
			l1[i+1] = l2[i];
		}
		l1[0] = e; 
	}
}
