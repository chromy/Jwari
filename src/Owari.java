import java.util.Random;

public class Owari {
	public static void main(String[] args) {
		Game game = new Game();
		
		System.out.println("Pick player 1.");
		Mover p1 = getMover();
		System.out.println("Pick player 2.");
		Mover p2 = getMover();
		Mover[] players = {p1, p2};
		String[] names = {"Player 1 - " + p1.getType(), "Player 2 - " + p2.getType()};
		
		while (!game.isOver()) {
			game.display();
			int cp = game.getCurrentPlayerId();

			if (!game.canCurrentPlayerMove()) {
				System.out.println(names[cp] + " can't move so must pass.");
				game.swapPlayers();
				continue;
			}
			
			int move = players[cp].getMove(game, names[cp]);
			assert (game.isValidMove(move)) : "Move not valid. Move was: " + move + " Player was: " + names[cp];
			game.move(move);
			//game = game.afterMove(move);
			System.out.println(names[cp] + " made the move: " + (move+1));
			
		}
		
		/*----------------------------------------------
		/int p1score = game.players[0].getScore();
		/int p2score = game.players[1].getScore();
		/if (p1score == p2score) {
		/	System.out.println("It was a draw!");
		/}
		/----------------------------------------------*/
		
		game.display();
		System.out.println("--------------------------------------");
		System.out.println(names[game.getLeadingPlayer()] + " Won!");
	}
	
	
	public static Mover getMover()
	{
		System.out.println("1. Human");
		System.out.println("2. Random");
		System.out.println("3. LookAheadOne");
		System.out.println("4. MinMax");
		System.out.println("5. AlphaBeta");
		int selection = IOUtil.readInt();
		switch (selection) {
			case 1:
				System.out.println("You selected Human.");
				return new Human();
			case 2:
				System.out.println("You selected RandomAI.");
				return new RandomAI(new Random());
			case 3:
				System.out.println("You selected LookAheadOneAI.");
				return new LookOneAI();
			case 4:
				System.out.println("You selected MinMaxAI.");
				return new MinMaxAI();
			case 5:
				System.out.println("You selected AlphaBetaAI.");
				return new AlphaBetaAI();
			default:
				System.out.println("Selection not valid - default to Human.");
				return new Human();
		}
	}
	

}
