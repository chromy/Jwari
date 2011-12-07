import java.util.Arrays;


public class HexAI extends Mover {
	public static String movertype = "HexAI";
	
	// Stats.
	private int playerId;
	private int nodesSearched = 0;
	private int nodesEval = 0;
	private int nodesTerm = 0;
	private int depthReached = 0;
	
	
	private final int maxDepth = 6;
	private int[] PV;
	private int[][] PVTri = makeTriArray(maxDepth+1);
	
	// Moral of the day; -Integer.MIN_VALUE != Integer.MAX_VALUE *cry*
	private static int MIN_VALUE = -100000;
	private static int MAX_VALUE = 100000;
	// Time for each move (in ms).
	private final long timeBudget = 5*1000;
	// True iff we'ver proved we can force a win.
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
		PVTri[0] = Arrays.copyOf(PVTri[2], maxDepth);
		long finishTime = System.currentTimeMillis() + timeBudget;
		clearStats(); 
		int score = 0;
		
		
		if (!forcewin) {
			long t3 = System.currentTimeMillis();
			//if (opponentMoveGuess == opponentMove) {
			//	score = IDalphabeta(game, PVTri, Math.max(depthReachedLastTurn-2, 0), finishTime);
			//} else {
			//	score = IDalphabeta(game, PVTri, 4, finishTime);
			//}
			score = IDalphabeta(game, PVTri, maxDepth, finishTime);
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
		
		if (score == MIN_VALUE || score == MAX_VALUE) {
			if (score == MIN_VALUE) {
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

		System.out.println("---------------------------------");



		Game afterPV = game.moveSequence(Arrays.copyOf(PV, Math.max(0, depthReached)));
		System.out.println("------------AFTER_PV-------------");
		afterPV.display();
		System.out.println("---------------------------------");
		
		//assert (bestmove >= 0) : "getMove returned an invalid move.";
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
		//System.out.println("PV Scores: " + java.util.Arrays.toString(PV_SCORES));
	}
	
	
	// iterative deepening
	private int IDalphabeta(Game game, int[][] principleVar, int depth, long finishTime) {
		depthReached = depth;
		int score = alphabeta(game, principleVar, 0, depth, MIN_VALUE, MAX_VALUE, 1);
		
		if (finishTime > System.currentTimeMillis() && maxDepth > depth) {
			return IDalphabeta(game, principleVar,  depth+1, finishTime);
		}
		
		for (int[] r : principleVar) {
			System.out.println(Arrays.toString(r));
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
			return color * evaluation(game, playerId);
		}
		
		// So if we need to stop 
		if (depth == limit) {
			// Quiescence search?
			nodesEval++;
			return color * evaluation(game, playerId); //qsearch(game, playerId, 4);
		}
		
		// If the current player can't actually move then it's the other players
		// turn again.
		if (!game.canCurrentPlayerMove()) {
			game = game.clone(); //Fix
			game.swapPlayers();
			return -alphabeta(game, principleVar, depth+1, 
							  limit, -beta, -alpha, -color);
		}
		
		/*
		int pmove = principleVar[0][depth];
		if (pmove >= 0 && game.isValidMove(pmove)) {
			// Get the score of this move.
			score = -alphabeta(game.afterMove(pmove), principleVar,
							   depth+1, limit, -beta, -alpha, -color);
			
			// Get move.
			if (score >= alpha) {
				for (int i=0; i<limit-depth-1; i++) {
					principleVar[depth][i+1] = principleVar[depth+1][i];
				}
				principleVar[depth][0] = pmove;
				
				System.out.println(depth + " " + limit);
				game.display();
				System.out.println(Arrays.toString(principleVar[depth]));
				game.moveSequence(Arrays.copyOf(principleVar[depth], limit-depth));
				
				alpha = score;
				//System.out.println("PV Changed: " + java.util.Arrays.toString(principleVar));
				//PV_SCORES[depth] = evaluation(game, playerId);
				
			}
			
			// AlphaBeta pruning.
			// Improve?
			if (beta <= alpha) {
				return alpha;
			}
		}

		*/
		int pmove = -1;
		// Otherwise consider each move.
		for (int move : game.allValidMoves()) {
			if (move >= 0 && move != pmove) {
				// Get the score of this move.
				//assert (game.isValidMove(move));
				score = -alphabeta(game.afterMove(move), principleVar,
								   depth+1, limit, -beta, -alpha, -color);
				//assert (game.isValidMove(move));
				// Get move.
				if (score > alpha) {
					//assert (game.isValidMove(move));
					testestes.copyandinsert(principleVar[depth], principleVar[depth+1], limit-depth-1, move);
					//for (int i=0; i<limit-depth; i++) {
					//	principleVar[depth][i+1] = principleVar[depth+1][i];
					//}
					//principleVar[depth][0] = move;
					//System.out.println(depth + " " + limit + " " + move);
					//Game a = game.clone();
					game.display();
					//System.out.println(Arrays.toString(principleVar[depth-1]));
					for (int[] r : principleVar) {
						System.out.println(Arrays.toString(r));
					}
					System.out.println(Arrays.toString(principleVar[depth]));
					System.out.println(Arrays.toString(principleVar[depth+1]));
					//System.out.println(a.getCurrentPlayerId());
					assert (game.isValidMove(principleVar[depth][0]));
					if (depth==0) {
						game.moveSequence(Arrays.copyOf(principleVar[depth], limit-depth));
					}
					assert (game.isValidMove(principleVar[depth][0]));
					
					alpha = score;
					//principleVar[depth] = move;
					//PV_SCORES[depth] = evaluation(game, playerId);
					
				}
				
				//AlphaBeta pruning.
				//Improve?
				//if (beta <= alpha) {
				//	break;
				//}
				
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
		if (game.isOver()) {
			return (currentplayer == game.getLeadingPlayer()) ? 1000 : -1000;
		}
		
		if (depth == 0) {
			return evaluation(game, currentplayer);
		}
		
		if (!game.canCurrentPlayerMove()) {
			game.swapPlayers();
			return qsearch(game, currentplayer, depth-1);
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
		
		Game tgame = game.afterMove(biggestPotId);
		if (tgame.isOver()) {
			return (currentplayer == game.getLeadingPlayer()) ? 1000 : -1000; 
		}
		
		game.move(smallestPotId);
		return qsearch(game, currentplayer, depth-1);
	}
	
	private int evaluation(Game game, int currentplayer) {
		if (game.isOver()) {
			nodesTerm++;
			return (currentplayer == game.getLeadingPlayer()) 
			                      ? MAX_VALUE : MIN_VALUE;
		}
		
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		return 100 + s1 - s2;
	}
	
	/*
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
		
		int myLargeBowls = 0;
		int myMedBowls = 0;
		int otherLargeBowls = 0;
		int oneBowls = 0;
		int zeroBowls = 0;
		int stones;
		
		// My side.
		for (int i=currentplayer*6; i<6+currentplayer*6; i++) {
			stones = game.bowls[i].getStones();
			if (stones >= 8) {
				myLargeBowls++;
			} else if (stones >= 6) {
				myMedBowls++;
			} else if (stones == 1) {
				oneBowls++;
			} else if (stones == 0) {
				zeroBowls++;
			}
		}
		
		int largeBowlFet = 5 * myLargeBowls;
		int medBowlFet = 3 * myMedBowls;
		
		// The other side.
		for (int i=((currentplayer+1)%2)*6; i<6+((currentplayer+1)%2)*6; i++) {
			stones = game.bowls[i].getStones();
			if ( stones >= 8) {
				otherLargeBowls++;
			}
			if (stones == 1) {
				oneBowls++;
			}
			if (stones == 0) {
				zeroBowls++;
			}
		}
		
		int guessP1 = 0;
		int guessP2 = 0;
		int tmp;
		while (myLargeBowls > 0 || otherLargeBowls > 0) {
			if (myLargeBowls > 0) {
				myLargeBowls--;
				guessP1 += oneBowls;
				tmp = oneBowls;
				oneBowls = zeroBowls;
				zeroBowls = tmp;
			}
			
			if (otherLargeBowls > 0) {
				otherLargeBowls--;
				guessP2 += oneBowls;
				tmp = oneBowls;
				oneBowls = zeroBowls;
				zeroBowls = tmp;
			}
		}
		
//		int LBFactor = 6;
//		int largebowls = 0;
//		for (int i=currentplayer*6; i<6+currentplayer*6; i++) {
//			if (game.bowls[i].getStones() >= 8) {
//				largebowls++;
//			}
//		}
//		int OBFactor = 1;
//		int onebowls = 0;
//		for (int i=((currentplayer+1)%2)*6; i<6+((currentplayer+1)%2)*6; i++) {
//			if (game.bowls[i].getStones() == 1) {
//				onebowls++;
//			}
//		}
		return 100 + s1 - s2 + guessP1 - guessP2; //+ largeBowlFet + medBowlFet; 
	}
	*/
}
