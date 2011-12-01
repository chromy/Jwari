
public class AlphaBetaAI extends Mover {
	public static String movertype = "AlphaBetaAI";
	private int playerId;
	private int depth = 15;
	private int[] killermoves;

	@Override
	public int getMove(Game game, String name) {
		playerId = game.getCurrentPlayerId();
		//ScorePair score = alphabeta(game, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		killermoves = new int[depth];
		for (int i=0; i<depth; i++) {
			killermoves[i] = -1;
		}
		ScorePair score = alphabeta(game, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		return score.getMove();
	}
	
	// Wikipedia & Computer Gamesmanship
	/**
	 * Recursive implementation of the alphabeta algorithm. Because we do the 
	 * first level by hand in getMove all the values are inverted.  
	 */
	private ScorePair alphabeta(Game game, int depth, int alpha, int beta, boolean maxplayer) {
		if (depth <= 0 || game.isOver()) {
			return new ScorePair(-1, evaluation(game, playerId));
		}
		
		int score;
		int bestmove = -1;
		
		if (!game.canCurrentPlayerMove()) {
			Game copy_game = game.clone(); //Fix
			copy_game.swapPlayers();
			return new ScorePair(-1, alphabeta(copy_game, depth-1, alpha, beta, !maxplayer).getScore());
		}
		if (maxplayer) {
			
			int kmove = killermoves[depth-1];
			if (kmove >= 0 && game.isValidMove(kmove)) {
	    		score = alphabeta(game.afterMove(kmove), depth-1, alpha, beta, !maxplayer).getScore();
	    		if (alpha < score) {
	    			alpha = score;
	    			bestmove = kmove;
	    		}
			}
			
			
		    for (int move : game.allValidMoves()) {
		    	if (move >= 0 && move != kmove) {
		    		score = alphabeta(game.afterMove(move), depth-1, alpha, beta, !maxplayer).getScore();
		    		if (alpha < score) {
		    			alpha = score;
		    			bestmove = move;
		    		}
		    		if (beta<=alpha) {
		    			killermoves[depth-1] = move;
		    			break;
		    		}
		    	}
		    }
		    return new ScorePair(bestmove, alpha);
		} else {
			int kmove = killermoves[depth-1];
			if (kmove >= 0 && game.isValidMove(kmove)) {
	    		score = alphabeta(game.afterMove(kmove), depth-1, alpha, beta, !maxplayer).getScore();
	    		if (beta > score) {
	    			beta = score;
	    			bestmove = kmove;
	    		}
			}
			
			
		    for (int move : game.allValidMoves()) {
		    	if (move >= 0 && move != kmove) {
		    		score = alphabeta(game.afterMove(move), depth-1, alpha, beta, !maxplayer).getScore();
		    		if (beta > score) {
		    			beta = score;
		    			bestmove = move;
		    		}
		    		if (beta<=alpha) {
		    			killermoves[depth-1] = move;
		    			break;
		    		}
		    	}
		    }
		    return new ScorePair(bestmove, beta);
		}
	}
	/*
	private ScorePair alphabeta(Game game, int depth, int alpha, int beta) {
		if (depth <= 0 || game.isOver()) {
			return new ScorePair(-1, evaluation(game, game.getCurrentPlayerId()));
		}
		
		int score;
		int bestmove = -1;
		
		if (!game.canCurrentPlayerMove()) {
			Game copy_game = game.clone(); //Fix
			copy_game.swapPlayers();
			return new ScorePair(-1, -(alphabeta(copy_game, depth-1, -beta, -alpha).getScore()));
		}
		
	    for (int move : game.allValidMoves()) {
	    	if (move >= 0) {
	    		score = -alphabeta(game.afterMove(move), depth-1, -beta, -alpha).getScore();
	    		if (alpha < score) {
	    			alpha = score;
	    			bestmove = move;
	    		}
	    		if (alpha >= beta) {
	    			break;
	    		}
	    	}
	    }
	    return new ScorePair(bestmove, alpha);
	    */
		
		
	
	private int evaluation(Game game, int currentplayer) {
		if (game.isOver()) {
			return (currentplayer == game.getLeadingPlayer() 
			                     ? Integer.MAX_VALUE : Integer.MIN_VALUE);

		}
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		return s1 - s2; 
	}
	
	@Override
	public String getType() {
		return movertype;
	}

}
