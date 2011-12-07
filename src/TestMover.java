public class TestMover {
	public static void main(String[] args) {
		System.out.println("How many games?");
		int numGames = IOUtil.readInt(); // n > 0
		System.out.println("Pick player 1.");
		Mover p1 = Owari.getMover();
		System.out.println("Pick player 2.");
		Mover p2 = Owari.getMover();
		
		
		int move = -1;
		Mover[] players = {p1, p2};
		int[] results = {0, 0};
		int totalturns = 0;
		long[] times = {0, 0};
		long timebefore;
		
		String[] names = {"Player 1 - " + p1.getType(), 
				          "Player 2 - " + p2.getType()};
		
		for (int i=0; i<numGames; i++) {
			Game game = new Game();
			int turns = 0;
			while (!game.isOver()) {
				int cp = game.getCurrentPlayerId();
				
				turns++;
				if (turns > 200) {
					System.out.println("Game went over " + turns + " turns. Stopping.");
					break;
				}
				if (!game.canCurrentPlayerMove()) {
					game.swapPlayers();
					continue;
				}
				
				timebefore = System.currentTimeMillis();
				move = players[cp].getMove(game, move, names[cp]);
				times[game.getCurrentPlayerId()] += System.currentTimeMillis() - timebefore;
				if (!game.isValidMove(move)) {
					System.out.println("Move not valid. Move was: " + move + " Player was: " + names[cp]);
					game.display();
					break;
				}
				//assert (game.isValidMove(move)) : "Move not valid. Move was: " + move + " Player was: " + names[cp];
				game.move(move);
				//game = game.afterMove(move);
			}
			
			totalturns += turns;
			results[game.getLeadingPlayer()]++;
		}
		
		System.out.println(names[0] + " won " + results[0] + " out of " + numGames + " games.");
		System.out.println(names[1] + " won " + results[1] + " out of " + numGames + " games.");
		System.out.println("Player 1: " + (results[0] / (double)numGames) + "%");
		System.out.println("Player 2: " + (results[1] / (double)numGames) + "%");
		System.out.println("The average number of turns in a game was: " + (totalturns / (double)numGames));
		System.out.println("Player 1 took: " + (times[0] / (totalturns/2))/1000.0 + "s on average per turn.");
		System.out.println("Player 2 took: " + (times[1] / (totalturns/2))/1000.0 + "s on average per turn.");
	}
}
