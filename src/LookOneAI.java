public class LookOneAI extends Mover {
	private static String movertype = "LookOneAI";

	@Override
	public int getMove(Game game, int opponentMove, String name) {
		int cp = game.getCurrentPlayerId();
		int bestmove = -1; //Ugly.
		int bestscore = Integer.MIN_VALUE;
		int score = 0;
		
		for (int move : game.allValidMoves()) {
			if (move >= 0) {
				Game aftermove = game.afterMove(move);
				score = evaluation(aftermove, cp);
				if (score >= bestscore) {
					bestmove = move;
					bestscore = score;
				}
			}
		}
		return bestmove;
	}
	
	private int evaluation(Game game, int currentplayer) {
		int s1 = game.getPlayer(currentplayer).getScore();
		int s2 = game.getPlayer((currentplayer+1)%2).getScore();
		return s1 - s2; 
	}
	
	@Override
	public String getType() {
		return movertype;
	}
}
