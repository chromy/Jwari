public class LookOneAI extends Mover {
	public static String movertype = "LookOneAI";

	@Override
	public int getMove(Game game, String name) {
		int cp = game.getCurrentPlayerId();
		int bestmove = -1; //Ugly.
		int bestscore = 0;
		for (int move : game.allValidMoves()) {
			if (move >= 0) {
				if (game.afterMove(move).players[cp].getScore() > bestscore) {
					bestmove = move;
					bestscore = game.afterMove(move).players[cp].getScore();
				}
			}
		}
		return bestmove;
	}

	public String getType() {
		return movertype;
	}
}
